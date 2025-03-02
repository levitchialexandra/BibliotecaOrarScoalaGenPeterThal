package ro.scoalaPeterThal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import basics.Book;
import basics.Loan;
import basics.Student;
import repository.BookRepository;
import repository.LoanRepository;
import repository.LoanRequest;
import repository.StudentRepository;

@Controller
public class LoanController {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    StudentRepository studentRepository;

    @PostMapping("/borrowBook/{id}")
    public ResponseEntity<String> borrowBook(@PathVariable Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();

            Loan loan = new Loan();
            loan.setBook(book);
            loan.setLoanDate(LocalDate.now());
            loan.setReturnDate(LocalDate.now().plusWeeks(2));

            loanRepository.save(loan);

            return ResponseEntity.ok("Cartea a fost împrumutată cu succes!");

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cartea nu a fost găsită!");
        }
    }

    @PostMapping("/createLoan")
    public ResponseEntity<?> createLoan(@RequestBody LoanRequest request) {

        if(request.getReturnDate().isBefore(LocalDate.now())){
            return ResponseEntity.badRequest().body("Data de returnare nu poate fi mai mică decât data curentă!");
        }
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(() -> new RuntimeException("Carte inexistentă"));
        Student student = studentRepository.findByEmail(request.getStudentEmail())
        .orElseGet(() -> {
            Student newStudent = new Student();
            newStudent.setName(request.getStudentName());
            newStudent.setEmail(request.getStudentEmail());
            newStudent.setAddress(request.getStudentAddress());
            newStudent.setPhone(request.getStudentPhone());
            return studentRepository.save(newStudent);
        });
                
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setStudent(student);
        loan.setLoanDate(LocalDate.now());
        loan.setReturnDate(request.getReturnDate());

        loanRepository.save(loan);

        return ResponseEntity.ok("Împrumut salvat cu succes");
    }

    @PostMapping("/returnBook/{id}")
    public ResponseEntity<String> returnBook(@PathVariable Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();

            Optional<Loan> loanOptional = loanRepository.findByBook(book);
            if (loanOptional.isPresent()) {
                System.out.println("Carte gasita!");
                Loan loan = loanOptional.get();
                loan.setReturnDate(LocalDate.now());
                loan.setReturned(true);
                loanRepository.save(loan);
            }

            return ResponseEntity.ok("Cartea a fost restituită cu succes!");

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cartea nu a fost găsită!");
        }
    }

    @GetMapping("/studentSearch")
    @ResponseBody
    public List<Student> searchStudents(@RequestParam String query) {
        return studentRepository.findByNameContainingIgnoreCase(query);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
