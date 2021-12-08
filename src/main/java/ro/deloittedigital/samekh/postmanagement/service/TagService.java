package ro.deloittedigital.samekh.postmanagement.service;

import java.util.Set;
import java.util.UUID;

public interface TagService {
    Set<String> saveAll(UUID postId, Set<String> tags);
}
