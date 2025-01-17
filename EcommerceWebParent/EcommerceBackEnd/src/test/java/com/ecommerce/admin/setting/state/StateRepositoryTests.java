package com.ecommerce.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.ecommerce.admin.setting.state.StateRepository;
import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTests {

	@Autowired
	private StateRepository repo;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testCreateCountry() {
		
		Integer countryId = 1;
		Country country = entityManager.find(Country.class, countryId);
		
		State state = repo.save(new State("West Bengal", country));
		
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateStateInUs() {
		Integer id = 2;
		Country country = entityManager.find(Country.class, id);
		
//		State state = repo.save(new State("Washington", country));
//		State state = repo.save(new State("California", country));
		State state = repo.save(new State("New York", country));
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListStateByCountry() {
		Integer countryId = 2;
		Country country = entityManager.find(Country.class, countryId);
		
		List<State> listStates = repo.findByCountryOrderByNameAsc(country);
		listStates.forEach(System.out::println);
		 
		assertThat(listStates.size()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateState() {
		int stateId = 3;
		String name = "Tamil Nadu";
		State state = repo.findById(stateId).get();
		
		state.setName(name);
		State updatedState = repo.save(state);
		
		assertThat(updatedState.getName()).isEqualTo(name);
	}
	
	@Test
	public void testGetState() {
		Integer stateId = 1;
		Optional<State> findById = repo.findById(stateId);
		assertThat(findById.isPresent());
	}
	
	@Test
	public void testDeleteState() {
		Integer stateId = 8;
		repo.deleteById(stateId);
		
		Optional<State> findById = repo.findById(stateId);
		assertThat(findById.isEmpty());
	}
}
