/**
 * 
 */
package com.example.demo.repository;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Book;

/**
 * @author ubarik
 *
 */
public interface BookRepository extends JpaRepository<Book, Long> {

	@Transactional(timeout=10)
	List<Book> findByName(String name);
	
	@Override
	@Transactional(timeout = 10)
	public List<Book> findAll();
}
