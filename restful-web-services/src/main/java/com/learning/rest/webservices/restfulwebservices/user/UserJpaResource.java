package com.learning.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.learning.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.learning.rest.webservices.restfulwebservices.jpa.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {

	
	private UserRepository userRepository;
	
	private PostRepository postRepository;

	// @Autowired
	public UserJpaResource(UserRepository userRepository, PostRepository postRepository) {
		this.userRepository = userRepository;
		 this.postRepository= postRepository;
	}

	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable Integer id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			throw new UserNotFoundException("id: " + id);
		}
		EntityModel<User> entityModel = EntityModel.of(user.get());
		
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));
		return entityModel;
	}

	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable Integer id) {
		userRepository.deleteById(id);
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrievePostsForUser(@PathVariable Integer id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			throw new UserNotFoundException("id: " + id);
		}
		return user.get().getPosts();
	}

	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User savedUser = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Post> createPostsForUser(@PathVariable Integer id, @Valid @RequestBody Post post) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			throw new UserNotFoundException("id: " + id);
		}
		post.setUser(user.get());
		
		Post savedPost = postRepository.save(post);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

}
