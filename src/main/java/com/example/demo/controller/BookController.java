/**
 * 
 */
package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.error.BookNotFoundException;
import com.example.demo.error.BookUnSupportedFieldPatchException;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ubarik
 *
 */
@RestController
@Slf4j
@RequestMapping("/bookapi")
@Api(value="Book Lib System")
public class BookController {
	@Autowired
	private BookRepository repository;
	private static final Logger log = LoggerFactory.getLogger(BookController.class);

	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@GetMapping("/books")
	@ApiOperation(value = "View a list of available books", response = List.class)
	List<Book> findAll() {
		log.info("finding list of books");
		return repository.findAll();
	}

	// Save
	@PostMapping("/addBook")
	//return 201 instead of 200
	@ResponseStatus(HttpStatus.CREATED)
	Book newBook(@RequestBody Book newBook) {
		return repository.save(newBook);
	}

	// Find
	@GetMapping("/book/{id}")
	Book findOne(@PathVariable Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new BookNotFoundException(id));
	}

	// Save or update
	@PutMapping("/updateBook/{id}")
	Book saveOrUpdate(@RequestBody Book newBook, @PathVariable Long id) {

		return repository.findById(id)
				.map(x -> {
					x.setName(newBook.getName());
					x.setAuthor(newBook.getAuthor());
					x.setPrice(newBook.getPrice());
					return repository.save(x);
				})
				.orElseGet(() -> {
					newBook.setId(id);
					return repository.save(newBook);
				});
	}

	// update author only
	@PatchMapping("/modifyBook/{id}")
	Book patch(@RequestBody Map<String, String> update, @PathVariable Long id) {

		return repository.findById(id)
				.map(x -> {

					String author = update.get("author");
					if (!StringUtils.isEmpty(author)) {
						x.setAuthor(author);

						// better create a custom method to update a value = :newValue where id = :id
						return repository.save(x);
					} else {
						throw new BookUnSupportedFieldPatchException(update.keySet());
					}

				})
				.orElseGet(() -> {
					throw new BookNotFoundException(id);
				});

	}

	@DeleteMapping("/delBook/{id}")
	void deleteBook(@PathVariable Long id) {
		repository.deleteById(id);
	}

}
