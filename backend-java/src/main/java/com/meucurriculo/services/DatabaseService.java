package com.meucurriculo.services;

import com.meucurriculo.entities.HardSkill;
import com.meucurriculo.entities.HardSkillProject;
import com.meucurriculo.entities.Project;
import com.meucurriculo.repositories.HardSkillProjectRepository;
import com.meucurriculo.repositories.HardSkillRepository;
import com.meucurriculo.repositories.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class DatabaseService {

    private final ProjectRepository projectRepository;
    private final HardSkillRepository hardSkillRepository;
    private final HardSkillProjectRepository hardSkillProjectRepository;

    public DatabaseService(ProjectRepository projectRepository,
                           HardSkillRepository hardSkillRepository,
                           HardSkillProjectRepository hardSkillProjectRepository) {
        this.projectRepository = projectRepository;
        this.hardSkillRepository = hardSkillRepository;
        this.hardSkillProjectRepository = hardSkillProjectRepository;
    }

    public boolean createMyResume() {
        List<HardSkill> hardSkills = Arrays.asList(
                new HardSkill("Java"), new HardSkill("Spring Boot"), new HardSkill("Spring Cloud"),
                new HardSkill("Spring MVC"), new HardSkill("REST"), new HardSkill("SQL"),
                new HardSkill("PostgreSQL"), new HardSkill("MySQL"), new HardSkill("DB2"),
                new HardSkill("SQLServer"), new HardSkill("Firebird"), new HardSkill("Oracle"),
                new HardSkill("Docker"), new HardSkill("Kafka"), new HardSkill("Flyway"),
                new HardSkill("JUnit"), new HardSkill("Mockito"), new HardSkill("Apache POI"),
                new HardSkill("Quarkus"), new HardSkill("Git"), new HardSkill("GitLab"),
                new HardSkill("GitHub"), new HardSkill("CI/CD"), new HardSkill("Angular"),
                new HardSkill("Angular 11"), new HardSkill("Angular 15"), new HardSkill("TypeScript"),
                new HardSkill("HTML"), new HardSkill("CSS"), new HardSkill("JSP"),
                new HardSkill("JSF"), new HardSkill("Caché"), new HardSkill("COS"),
                new HardSkill("GWT"), new HardSkill("Eureka"), new HardSkill("Jira")
        );
        hardSkillRepository.saveAll(hardSkills);

        Project p202507 = saveProject("montreal-project-2", 2025, Month.JULY, LocalDate.now());
        link(p202507, "montreal-project-2-description",
                getHardSkillByName("Java"), getHardSkillByName("Spring Boot"), getHardSkillByName("REST"),
                getHardSkillByName("Oracle"), getHardSkillByName("JSP"), getHardSkillByName("JSF"),
                getHardSkillByName("Git"), getHardSkillByName("GitLab"));

        Project p202405 = saveProject("montreal-project-1", 2024, Month.MAY,
                LocalDate.of(2025, Month.JULY, 1));
        link(p202405, "montreal-project-1-description",
                getHardSkillByName("Java"), getHardSkillByName("Spring Boot"), getHardSkillByName("DB2"),
                getHardSkillByName("SQLServer"), getHardSkillByName("Firebird"), getHardSkillByName("JUnit"),
                getHardSkillByName("Mockito"), getHardSkillByName("Apache POI"), getHardSkillByName("Angular 15"),
                getHardSkillByName("TypeScript"), getHardSkillByName("HTML"), getHardSkillByName("CSS"),
                getHardSkillByName("CI/CD"), getHardSkillByName("Eureka"), getHardSkillByName("Git"),
                getHardSkillByName("GitLab"));

        Project p202304 = saveProject("montreal-project-0", 2023, Month.APRIL,
                LocalDate.of(2024, Month.MAY, 1));
        link(p202304, "montreal-project-0-description",
                getHardSkillByName("Java"), getHardSkillByName("Spring Boot"), getHardSkillByName("Spring Cloud"),
                getHardSkillByName("DB2"), getHardSkillByName("Git"), getHardSkillByName("GitLab"));

        Project p202408 = saveProject("erigos-overcrux", 2024, Month.AUGUST, LocalDate.now());
        link(p202408, "erigos-overcrux-description",
                getHardSkillByName("Java"), getHardSkillByName("Spring Boot"), getHardSkillByName("SQL"),
                getHardSkillByName("PostgreSQL"), getHardSkillByName("Git"), getHardSkillByName("GitHub"),
                getHardSkillByName("Angular 11"), getHardSkillByName("TypeScript"), getHardSkillByName("HTML"),
                getHardSkillByName("CSS"));

        Project p202206 = saveProject("shift-project-1", 2022, Month.JUNE,
                LocalDate.of(2023, Month.FEBRUARY, 1));
        link(p202206, "shift-project-1-description",
                getHardSkillByName("Java"), getHardSkillByName("PostgreSQL"), getHardSkillByName("JUnit"),
                getHardSkillByName("Quarkus"), getHardSkillByName("Git"), getHardSkillByName("Angular"),
                getHardSkillByName("TypeScript"), getHardSkillByName("HTML"), getHardSkillByName("CSS"),
                getHardSkillByName("Caché"), getHardSkillByName("GitLab"));

        Project p202202 = saveProject("shift-project-0", 2022, Month.FEBRUARY,
                LocalDate.of(2022, Month.JUNE, 1));
        link(p202202, "shift-project-0-description",
                getHardSkillByName("Java"), getHardSkillByName("Quarkus"), getHardSkillByName("Git"),
                getHardSkillByName("GitLab"), getHardSkillByName("Caché"), getHardSkillByName("COS"));

        Project p202111 = saveProject("monitora-project-1", 2021, Month.NOVEMBER,
                LocalDate.of(2022, Month.JANUARY, 1));
        link(p202111, "monitora-project-1-description",
                getHardSkillByName("Java"), getHardSkillByName("Spring Boot"), getHardSkillByName("PostgreSQL"),
                getHardSkillByName("Docker"), getHardSkillByName("Kafka"), getHardSkillByName("Flyway"),
                getHardSkillByName("JUnit"), getHardSkillByName("Mockito"), getHardSkillByName("CI/CD"),
                getHardSkillByName("Jira"), getHardSkillByName("Git"), getHardSkillByName("GitLab"));

        Project p202104 = saveProject("monitora-project-0", 2021, Month.APRIL,
                LocalDate.of(2021, Month.OCTOBER, 1));
        link(p202104, "monitora-project-0-description",
                getHardSkillByName("Java"), getHardSkillByName("Spring MVC"), getHardSkillByName("GWT"),
                getHardSkillByName("Git"), getHardSkillByName("GitLab"));

        Project p201905 = saveProject("infoway-project-1", 2019, Month.MAY,
                LocalDate.of(2021, Month.APRIL, 1));
        link(p201905, "infoway-project-1-description",
                getHardSkillByName("Java"), getHardSkillByName("PostgreSQL"), getHardSkillByName("MySQL"),
                getHardSkillByName("Jira"), getHardSkillByName("Git"), getHardSkillByName("GitLab"));

        Project p201811 = saveProject("infoway-project-0", 2018, Month.NOVEMBER,
                LocalDate.of(2019, Month.APRIL, 1));
        link(p201811, "infoway-project-0-description",
                getHardSkillByName("Java"), getHardSkillByName("Spring Boot"), getHardSkillByName("PostgreSQL"),
                getHardSkillByName("JUnit"), getHardSkillByName("Mockito"), getHardSkillByName("Git"),
                getHardSkillByName("GitLab"));

        hardSkillProjectRepository.saveAll(collectAll(
                p202507, p202405, p202304, p202408, p202206, p202202, p202111, p202104, p201905, p201811
        ));

        return true;
    }

    private Project saveProject(String name, int year, Month startMonth, LocalDate endDate) {
        Project project = new Project(name, LocalDate.of(year, startMonth, 1), endDate);
        return projectRepository.save(project);
    }

    private void link(Project project, String description, HardSkill... skills) {
        for (HardSkill skill : skills) {
            if (skill != null) {
                project.addHardSkill(skill, description, project.getJoinDate(), project.getExitDate());
            }
        }
    }

    private List<HardSkillProject> collectAll(Project... projects) {
        List<HardSkillProject> list = new ArrayList<>();
        for (Project p : projects) {
            list.addAll(p.getAllHardSkillProjects());
        }
        return list;
    }

    private HardSkill getHardSkillByName(String name) {
        return hardSkillRepository.findByName(name);
    }
}
