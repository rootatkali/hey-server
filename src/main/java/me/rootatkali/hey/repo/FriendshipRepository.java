package me.rootatkali.hey.repo;

import me.rootatkali.hey.model.Friendship;
import me.rootatkali.hey.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FriendshipRepository extends CrudRepository<Friendship, String> {
  @Query("select f from Friendship f where f.invitor = ?1 or f.invitee = ?1")
  Iterable<Friendship> getFriendshipByUser(User u);
  
  @Query("select f from Friendship f where (f.invitor = ?1 or f.invitee = ?1) and f.status = ?2")
  Iterable<Friendship> getFriendshipByUserAndStatus(User u, Friendship.Status status);
  
  @Query("select f from Friendship f where f.invitor = ?1")
  Iterable<Friendship> getFriendRequestsByInvitor(User u);
  
  @Query("select f from Friendship f where f.invitee = ?1 and f.status = ?2")
  Iterable<Friendship> getPendingRequestsByUserAndStatus(User u, Friendship.Status status);
  
  @Query("select f from Friendship f where (f.invitor = ?1 and f.invitee = ?2) or (f.invitor = ?2 and f.invitee = ?1)")
  Optional<Friendship> findByTwoUsers(User a, User b);
}
