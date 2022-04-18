package me.rootatkali.hey.repo;

import me.rootatkali.hey.model.Friendship;
import me.rootatkali.hey.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface FriendshipRepository extends CrudRepository<Friendship, String> {
  @Query("select f from Friendship f where f.invitor = ?1 or f.invitee = ?1")
  Iterable<Friendship> getFriendshipByUser(User u);
  
  @Query("select f from Friendship f where (f.invitor = ?1 or f.invitee = ?1) and f.status = ?2")
  Iterable<Friendship> getFriendshipByUserAndStatus(User u, Friendship.Status status);
  
  @Query("select f from Friendship f where f.invitor = ?1")
  Iterable<Friendship> getFriendRequestsByInvitor(User u);
  
  @Query("select f from Friendship f where f.invitee = ?1 and f.status = ?2")
  Iterable<Friendship> getPendingRequestsByUserAndStatus(User u, Friendship.Status status);
}