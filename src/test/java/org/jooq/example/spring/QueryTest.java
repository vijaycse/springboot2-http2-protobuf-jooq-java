package org.jooq.example.spring;

import static java.util.Arrays.asList;
import static org.jooq.example.db.h2.Tables.AUTHOR;
import static org.jooq.example.db.h2.Tables.BOOK;
import static org.jooq.example.db.h2.Tables.BOOK_STORE;
import static org.jooq.example.db.h2.Tables.BOOK_TO_BOOK_STORE;
import static org.jooq.impl.DSL.countDistinct;
import static org.junit.Assert.assertEquals;

import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.example.db.h2.tables.Author;
import org.jooq.example.db.h2.tables.Book;
import org.jooq.example.db.h2.tables.BookStore;
import org.jooq.example.db.h2.tables.BookToBookStore;
import org.jooq.example.db.h2.tables.records.BookRecord;
import com.jooq.h2.spring.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Lukas Eder
 * @author Thomas Darimont
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class QueryTest {

	@Autowired DSLContext create;

	@Test
	public void testJoin() throws Exception {
		// All of these tables were generated by jOOQ's Maven plugin
		Book b = BOOK.as("b");
		Author a = AUTHOR.as("a");
		BookStore s = BOOK_STORE.as("s");
		BookToBookStore t = BOOK_TO_BOOK_STORE.as("t");

		Result<Record3<String, String, Integer>> result = create.select(a.FIRST_NAME, a.LAST_NAME, countDistinct(s.NAME))
				.from(a).join(b).on(b.AUTHOR_ID.equal(a.ID)).join(t).on(t.BOOK_ID.equal(b.ID)).join(s)
				.on(t.BOOK_STORE_NAME.equal(s.NAME)).groupBy(a.FIRST_NAME, a.LAST_NAME).orderBy(countDistinct(s.NAME).desc())
				.fetch();

		assertEquals(2, result.size());
		assertEquals("Paulo", result.getValue(0, a.FIRST_NAME));
		assertEquals("George", result.getValue(1, a.FIRST_NAME));

		assertEquals("Coelho", result.getValue(0, a.LAST_NAME));
		assertEquals("Orwell", result.getValue(1, a.LAST_NAME));

		assertEquals(Integer.valueOf(3), result.getValue(0, countDistinct(s.NAME)));
		assertEquals(Integer.valueOf(2), result.getValue(1, countDistinct(s.NAME)));
	}

	@Test
	public void testActiveRecords() throws Exception {
		Result<BookRecord> result = create.selectFrom(BOOK).orderBy(BOOK.ID).fetch();

		assertEquals(4, result.size());
		assertEquals(asList(1, 2, 3, 4), result.getValues(0));
	}
}
