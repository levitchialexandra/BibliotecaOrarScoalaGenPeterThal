package ro.scoalaPeterThal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import repository.BookRepository;
import repository.GenreRepository;
import repository.LoanRepository;
import basics.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class BookContrl {
	@Autowired
	BookRepository bookRepository;

	@Autowired
	GenreRepository genreRepository;

	@Autowired
	LoanRepository loanRepository;
	@Autowired
	private HttpSession session;

	@GetMapping("/books")
	public String BooksList() {
		return "bookslist";
	}

	public List<Genre> getAllGenres() {
		return genreRepository.findAll();
	}

	@GetMapping("/pages/addBook")
	public String showAddBookForm(Model model) {
		if (!CheckIfUserIsLoggedIn())
			return "redirect:/pages/login";
		model.addAttribute("book", new Book());
		model.addAttribute("genres", genreRepository.findAll());
		return "pages/addBook";
	}

	@PostMapping("/pages/addBook")
	public String addBook(@Valid @ModelAttribute Book book, BindingResult result, Model model) {

		if (result.hasErrors()) {

			model.addAttribute("genres", genreRepository.findAll());
			return "pages/addBook";
		}
		bookRepository.save(book);
		model.addAttribute("genres", genreRepository.findAll());
		model.addAttribute("book", new Book());
		return "pages/addBook";
	}

	@DeleteMapping("/deleteBook/{id}")
	public ResponseEntity<String> deleteBook(@PathVariable Long id) {

		Optional<Book> bookOptional = bookRepository.findById(id);
		if (bookOptional.isPresent()) {
			bookRepository.delete(bookOptional.get());
			return ResponseEntity.ok("Cartea a fost ștearsă cu succes!");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cartea nu a fost găsită!");
		}
	}

	
	public Boolean CheckIfUserIsLoggedIn() {
		AppUser sessionUser = (AppUser) session.getAttribute("sessionUser");

		return sessionUser != null;
	}

	@GetMapping("/booksAjax")
	@ResponseBody
	public Map<String, Object> getBooks(@RequestParam(required = false) String title,
			@RequestParam int draw, @RequestParam(required = false) String author,
			@RequestParam(required = false) Integer publicationYear,
			@RequestParam(value = "genre", required = false) String genreId,
			@RequestParam(value = "search[value]", required = false) String searchValue,
			@RequestParam(defaultValue = "0") int start,
			@RequestParam(defaultValue = "10") int length,
			@RequestParam(value = "order[0][column]", defaultValue = "0") int orderColumn,
			@RequestParam(value = "order[0][dir]", defaultValue = "asc") String orderDir) {

		Pageable pageable = PageRequest.of(start / length, length, "asc".equals(orderDir)
				? Sort.by(orderColumn == 0 ? "title" : orderColumn == 1 ? "author" : "publicationYear")
				: Sort.by(orderColumn == 0 ? "title" : orderColumn == 1 ? "author" : "publicationYear").descending());
		Long genId = null;
		if (genreId != "") {
			List<Genre> genres = genreRepository.findFirstByNameContaining(genreId);

			System.out.println(genreId + " " + genres);
			genId = (genres != null && !genres.isEmpty()) ? genres.get(0).getId() : null;
		}
		List<Book> books = searchBooksByFilter(title, author, genId, publicationYear, searchValue, pageable);

		Map<String, Object> response = new HashMap<>();
		response.put("draw", draw);
		response.put("recordsTotal", bookRepository.count());
		response.put("recordsFiltered", books.size());
		response.put("data", books);

		return response;
	}

	@GetMapping("/booksAjaxWithAvailabilityWithPag")
	@ResponseBody
	public Map<String, Object> getBooksWithAvailabilityWithPag(
			@RequestParam(required = false) String title, @RequestParam int draw,
			@RequestParam(required = false) String author,
			@RequestParam(required = false) Integer publicationYear,
			@RequestParam(value = "genre", required = false) String genreId,
			@RequestParam(value = "search[value]", required = false) String searchValue,
			@RequestParam(defaultValue = "0") int start,
			@RequestParam(defaultValue = "10") int length,
			@RequestParam(value = "order[0][column]", defaultValue = "0") int orderColumn,
			@RequestParam(value = "order[0][dir]", defaultValue = "asc") String orderDir,
			@RequestParam(required = false) String availability) {

		Pageable pageable = PageRequest.of(start / length, length, "asc".equals(orderDir)
				? Sort.by(orderColumn == 0 ? "title" : orderColumn == 1 ? "author" : "publicationYear")
				: Sort.by(orderColumn == 0 ? "title" : orderColumn == 1 ? "author" : "publicationYear").descending());

		Long genId = null;
		if (genreId != "") {
			List<Genre> genres = genreRepository.findFirstByNameContaining(genreId);
			genId = (genres != null && !genres.isEmpty()) ? genres.get(0).getId() : null;
		}

		List<Book> books = searchBooksByFilter(title, author, genId, publicationYear, searchValue, pageable);
		List<BookDTO> bookDTOs = new ArrayList<>();

		for (Book book : books) {
			BookDTO bookDTO = new BookDTO();
			var avab = getAvailabilityStatus(book);
			boolean addBook = true;

			if (addBook) {
				bookDTO.setId(book.getId());
				bookDTO.setTitle(book.getTitle());
				bookDTO.setAuthor(book.getAuthor());
				bookDTO.setGenre(book.getGenre());
				bookDTO.setPublicationYear(book.getPublicationYear());
				bookDTO.setAvailability(avab);
				bookDTOs.add(bookDTO);
			}
		}

		long totalFilteredRecords = bookDTOs.size();
		long totalRecords = bookRepository.count();

		int toIndex = (int) Math.min(start + length, totalFilteredRecords);
		List<BookDTO> paginatedBooks = bookDTOs.subList(start, toIndex);

		Map<String, Object> response = new HashMap<>();
		response.put("draw", draw);
		response.put("recordsTotal", totalRecords); // Total cărți (fără filtrare)
		response.put("recordsFiltered", totalFilteredRecords); // Total cărți filtrate
		response.put("data", paginatedBooks);

		return response;
	}

	@GetMapping("/booksAjaxWithAvailability")
	@ResponseBody
	public Map<String, Object> getBooksWithAvailability(@RequestParam(value = "title", required = false) String title,
			@RequestParam int draw, @RequestParam(value = "author", required = false) String author,
			@RequestParam(required = false) Integer publicationYear,
			@RequestParam(value = "genre", required = false) String genreId,
			@RequestParam(value = "search[value]", required = false) String searchValue,

			@RequestParam(value = "order[0][column]", defaultValue = "0") int orderColumn,
			@RequestParam(value = "order[0][dir]", defaultValue = "asc") String orderDir,
			@RequestParam(required = false) String availability) {

		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, "asc".equals(orderDir)
				? Sort.by(orderColumn == 0 ? "title" : orderColumn == 1 ? "author" : "publicationYear")
				: Sort.by(orderColumn == 0 ? "title" : orderColumn == 1 ? "author" : "publicationYear").descending());

		Long genId = null;
		if (genreId != "") {
			List<Genre> genres = genreRepository.findFirstByNameContaining(genreId);
			genId = (genres != null && !genres.isEmpty()) ? genres.get(0).getId() : null;
		}

		List<Book> books = searchBooksByFilter(title, author, genId, publicationYear, searchValue, pageable);
		List<BookDTO> bookDTOs = new ArrayList<>();

		for (Book book : books) {
			BookDTO bookDTO = new BookDTO();
			var avabStat = getAvailabilityStatus(book);
			System.out.println(avabStat);
			if (availability == "" || (avabStat.equalsIgnoreCase(availability)
					|| avabStat.contains(availability))) {
				bookDTO.setId(book.getId());
				bookDTO.setTitle(book.getTitle());
				bookDTO.setAuthor(book.getAuthor());
				bookDTO.setGenre(book.getGenre());
				bookDTO.setPublicationYear(book.getPublicationYear());
				bookDTO.setAvailability(getAvailabilityStatus(book));
				bookDTOs.add(bookDTO);
			}

		}

		long totalFilteredRecords = bookDTOs.size();
		long totalRecords = bookRepository.count();

		Map<String, Object> response = new HashMap<>();
		response.put("draw", draw);
		response.put("recordsTotal", totalRecords);
		response.put("recordsFiltered", totalFilteredRecords);
		response.put("data", bookDTOs);

		return response;
	}

	public List<Book> searchBooksByFilter(String title, String author, Long genreId, Integer publicationYear,
			String searchValue, Pageable pageable) {

		return bookRepository.findBooksByCriteria(title, author, genreId, publicationYear, searchValue, pageable);
	}

	public String getAvailabilityStatus(Book book) {

		for (Loan loan : book.getLoans()) {
			if (loan.getReturnDate().isAfter(LocalDate.now())) {
				return "Împrumutat până la " + loan.getReturnDate();
			}
			else{
				return "Termen depasit! Data limită " + loan.getReturnDate();
			}
		}
		return "Disponibil";
	}

	public List<BookDTO> filterBooksByAvailability(List<BookDTO> bookDTOs, String filterType) {
		return bookDTOs.stream()
				.filter(bookDTO -> {
					if ("ALL".equalsIgnoreCase(filterType)) {
						return true; 
					} else if ("DISPONIBIL".equalsIgnoreCase(filterType)) {
						return "Disponibil".equals(bookDTO.getAvailability());
					} else if ("Împrumutat".equalsIgnoreCase(filterType)) {
						return bookDTO.getAvailability().contains("Împrumutat");
					} else if ("Termen".equalsIgnoreCase(filterType)) {
						return bookDTO.getAvailability().contains("Termen");
					}
					return false;
				})
				.collect(Collectors.toList());
	}

}
