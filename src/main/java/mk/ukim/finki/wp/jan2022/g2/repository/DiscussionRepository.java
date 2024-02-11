package mk.ukim.finki.wp.jan2022.g2.repository;

import com.gargoylesoftware.htmlunit.javascript.host.intl.DateTimeFormat;
import mk.ukim.finki.wp.jan2022.g2.model.Discussion;
import mk.ukim.finki.wp.jan2022.g2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    List<Discussion> findAllByParticipantsContaining(User user);
    List<Discussion> findAllByDueDateBefore(LocalDate date);
    List<Discussion> findAllByParticipantsContainingAndDueDateBefore(User user, LocalDate date);
}
