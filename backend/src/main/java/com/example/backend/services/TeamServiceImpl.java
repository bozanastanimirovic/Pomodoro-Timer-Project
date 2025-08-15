package com.example.backend.services;

import java.util.List;

import com.example.backend.services.impl.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.backend.models.Team;
import com.example.backend.models.User;
import com.example.backend.repositories.TeamRepository;
import com.example.backend.repositories.UserRepository;

@AllArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

	private final UserRepository userRepository;
	
	public List<Team> getAll() {
		return teamRepository.findAll();
	}

	@Override
	public Team getById(Long id) {
		return teamRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: Team is not found."));
	}

	@Override
	public boolean existsById(Long id) {
		return teamRepository.existsById(id);
	}

	@Override
	public Team create(Team t) {
		return teamRepository.save(t);
	}

	@Override
	public void delete(Long id) {
		teamRepository.deleteById(id);
	}

	@Override
	public List<User> getTeamMembers(Long teamId) {
        return teamRepository.findById(teamId).map(Team::getUsers).orElseThrow(() -> new RuntimeException("Error: Team is not found."));
	}
	

	@Override
	 public void addUsersToTeam(Long teamId, List<Long> userIds) {
        teamRepository.findById(teamId).map(team -> {
			List<User> usersToAdd = userIds.stream()
					.map(userId -> userRepository.findById(userId)
							.orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found.")))
					.filter(user -> !team.getUsers().contains(user))
					.toList();
			team.getUsers().addAll(usersToAdd);
			return teamRepository.save(team);
		}).orElseThrow(() -> new RuntimeException("Team with ID " + teamId + " not found."));

	 }
	 
     @Override
	 public Team addUserToTeam(Long teamId, Long userId) {
		 teamRepository.findById(teamId).map(team -> {
			 User user = userRepository.findById(userId)
					 .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found."));

			 if (!team.getUsers().contains(user)) {
				 team.getUsers().add(user);
			 }
			 return teamRepository.save(team);
		 }).orElseThrow(() -> new RuntimeException("Team with ID " + teamId + " not found."));
		 return null;
	 }
	 
     @Override
	 public Team removeUserFromTeam(Long teamId, Long userId) {
		 teamRepository.findById(teamId).map(team -> {
			 User user = userRepository.findById(userId)
					 .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found."));

			 if (team.getUsers().contains(user)) {
				 team.getUsers().remove(user);
				 return teamRepository.save(team);
			 } else {
				 throw new RuntimeException("User is not a member of the team.");
			 }
		 }).orElseThrow(() -> new RuntimeException("Team with ID " + teamId + " not found."));
		 return null;
	 }


    @Override
    public boolean existsByTeamName(String teamName) {
        return teamRepository.existsByTeamName(teamName);
    }

    @Override
	public Team updateName(String teamName, Long id) {
		teamRepository.findById(id).map((team) ->{
		team.setTeamName(teamName);
		return teamRepository.save(team);
		}).orElseThrow(() -> new RuntimeException("Error: Team is not found."));

        return null;
    }
}
