package mk.ukim.finki.wp.jan2022.g2.service.impl;

import mk.ukim.finki.wp.jan2022.g2.model.Discussion;
import mk.ukim.finki.wp.jan2022.g2.model.DiscussionTag;
import mk.ukim.finki.wp.jan2022.g2.model.User;
import mk.ukim.finki.wp.jan2022.g2.model.exceptions.InvalidDiscussionIdException;
import mk.ukim.finki.wp.jan2022.g2.repository.DiscussionRepository;
import mk.ukim.finki.wp.jan2022.g2.repository.UserRepository;
import mk.ukim.finki.wp.jan2022.g2.service.DiscussionService;
import mk.ukim.finki.wp.jan2022.g2.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
@Service
public class DiscussionServiceImpl implements DiscussionService {
    private final DiscussionRepository repository;
    private final UserRepository userRepository;
    private final UserService userService;

    public DiscussionServiceImpl(DiscussionRepository repository, UserRepository userRepository, UserService userService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public List<Discussion> listAll() {
        return repository.findAll();
    }

    @Override
    public Discussion findById(Long id) {
        return repository.findById(id).orElseThrow(InvalidDiscussionIdException::new);
    }

    @Override
    public Discussion create(String title, String description, DiscussionTag discussionTag, List<Long> participants, LocalDate dueDate) {
        List<User> users = userRepository.findAllById(participants);
        Discussion discussion = new Discussion(title, description, discussionTag, users, dueDate);
        return repository.save(discussion);
    }

    @Override
    public Discussion update(Long id, String title, String description, DiscussionTag discussionTag, List<Long> participants) {
        List<User> users = userRepository.findAllById(participants);
        Discussion discussion = findById(id);
        discussion.setTitle(title);
        discussion.setDescription(description);
        discussion.setTag(discussionTag);
        discussion.setParticipants(users);
        return repository.save(discussion);
    }

    @Override
    public Discussion delete(Long id) {
        Discussion discussion = findById(id);
        repository.delete(discussion);
        return discussion;
    }

    @Override
    public Discussion markPopular(Long id) {
        Discussion discussion = findById(id);
        discussion.setPopular(true);
        return repository.save(discussion);
    }

    @Override // TODO: implement
    public List<Discussion> filter(Long participantId, Integer daysUntilClosing) {
        if (participantId == null && daysUntilClosing == null) {
            return listAll();
        } else if (participantId == null) {
            return repository.findAllByDueDateBefore(LocalDate.now().plusDays(daysUntilClosing));
        } else if (daysUntilClosing == null) {
            return repository.findAllByParticipantsContaining(userService.findById(participantId));
        } else
            return repository.findAllByParticipantsContainingAndDueDateBefore(userService.findById(participantId), LocalDate.now().plusDays(daysUntilClosing));
    }
}
