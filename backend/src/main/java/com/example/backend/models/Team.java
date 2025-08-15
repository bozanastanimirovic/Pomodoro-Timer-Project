package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;
    @Column(nullable = false, name = "team_name")
    private String teamName;

    @ManyToMany
	@JsonIgnore
    @JoinTable(
            name = "user_team",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Task> tasks = new ArrayList<>();
    

    public Team(){}

	public Team( String teamName, List<Task> tasks, List<User> users) {
		super();
		this.teamName = teamName;
		this.tasks = tasks;
		this.users = users;
	}

    public Team(String teamName){
        this.teamName = teamName;
        this.tasks = new ArrayList<>();
        this.users = new ArrayList<>();

    }


}
