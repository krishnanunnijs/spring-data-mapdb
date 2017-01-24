package org.springframework.data.mapdb.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BookExampleConfiguration.class })
public class BookMapDbExampleTests {

	private static Object[][] BOOKS_DATA = new Object[][] {
			new Object[] { "0-7475-3269-9", "Harry Potter and the Philosopher's Stone", 256, "Fantasy", "J. K. Rowling", LocalDate.of(1998, 9, 1), "Arthur A. Levine / Scholastic" },
			new Object[] { "0-7475-5100-6", "Harry Potter and the Order of the Phoenix", 870, "Fantasy", "J. K. Rowling", LocalDate.of(2003, 6, 21), "Arthur A. Levine / Scholastic" },
			new Object[] { "0-7475-8108-8", "Harry Potter and the Half-Blood Prince", 652, "Fantasy", "J. K. Rowling", LocalDate.of(2005, 7, 16), "Arthur A. Levine / Scholastic" } };

	@Autowired
	private BookRepository bookRepository;

	private List<Book> books;

	@Before
	public void setup() {
		books = new ArrayList<>(3);
		for (Object[] item : BOOKS_DATA) {
			Book book = new Book();
			book.setIsbn((String) item[1]);
			book.setTitle((String) item[2]);
			book.setPages((int) item[3]);
			book.setGenre((String) item[4]);
			book.setAuthor((String) item[5]);
			book.setPublicationDate((LocalDate) item[6]);
			book.setPublisher((String) item[7]);
		}
	}

	@Test
	public void testExample() {

		bookRepository.save(books);

		Assert.assertEquals(books.get(0), bookRepository.findOne(books.get(0).getIsbn()));

		List<Book> booksFromRepository = bookRepository.findByTitle(books.get(0).getTitle());
		Assert.assertTrue(CollectionUtils.isNotEmpty(booksFromRepository));
		Assert.assertEquals(books.get(0), booksFromRepository.get(0));
		booksFromRepository.clear();

		booksFromRepository = bookRepository.findByGenre(books.get(0).getGenre(), new Sort(Direction.DESC, "pages"));
		Assert.assertTrue(CollectionUtils.isNotEmpty(booksFromRepository));
		Assert.assertEquals(3, booksFromRepository.size());
		Assert.assertEquals(books.get(3), booksFromRepository.get(0));
		booksFromRepository.clear();

		Iterable<Book> iBooks = bookRepository.findAll(new Sort(Direction.ASC, "publicationDate"));
		Assert.assertTrue(iBooks.iterator().hasNext());
		Assert.assertEquals(books.get(0), iBooks.iterator().next());

		booksFromRepository = bookRepository.findByGenreAndPages(books.get(0).getGenre(), books.get(0).getPages());
		Assert.assertTrue(CollectionUtils.isNotEmpty(booksFromRepository));
		Assert.assertEquals(books.get(0), booksFromRepository.get(0));
	}

}
