package ro.deloittedigital.samekh.postmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.deloittedigital.samekh.postmanagement.model.domain.Reaction;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, UUID> {
    Optional<Reaction> getByPostIdAndUserId(UUID postId, UUID userId);
}
