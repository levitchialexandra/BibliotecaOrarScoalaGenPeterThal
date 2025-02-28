package repository;
import org.springframework.data.jpa.domain.Specification;

import basics.Book;

public class BookSpecification {

    public static Specification<Book> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title != null && !title.isEmpty()) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
            }
            return null;
        };
    }

    public static Specification<Book> hasAuthor(String author) {
        return (root, query, criteriaBuilder) -> {
            if (author != null && !author.isEmpty()) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), "%" + author.toLowerCase() + "%");
            }
            return null;
        };
    }

    public static Specification<Book> hasGenre(Long genreId) {
        return (root, query, criteriaBuilder) -> {
            if (genreId != null) {
                return criteriaBuilder.equal(root.get("genre").get("id"), genreId);
            }
            return null;
        };
    }

    public static Specification<Book> hasPublicationYear(Integer publicationYear) {
        return (root, query, criteriaBuilder) -> {
            if (publicationYear != null) {
                return criteriaBuilder.equal(root.get("publicationYear"), publicationYear);
            }
            return null;
        };
    }

    public static Specification<Book> buildSpecification(String title, String author, Long genreId, Integer publicationYear) {
        Specification<Book> spec = Specification.where(null);
        
        if (title != null && !title.isEmpty()) {
            spec = spec.and(hasTitle(title));
        }
        if (author != null && !author.isEmpty()) {
            spec = spec.and(hasAuthor(author));
        }
        if (genreId != null) {
            spec = spec.and(hasGenre(genreId));
        }
        if (publicationYear != null) {
            spec = spec.and(hasPublicationYear(publicationYear));
        }

        return spec;
    }
}
