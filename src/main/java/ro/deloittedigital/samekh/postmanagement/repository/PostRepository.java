package ro.deloittedigital.samekh.postmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.deloittedigital.samekh.postmanagement.model.domain.Post;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> getAllByUserId(UUID userId);
}
