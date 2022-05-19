package me.rootatkali.hey.model;

import java.util.List;

/**
 * The client view for a friend.
 *
 * @param id The user ID
 * @param username The username (display username)
 * @param firstName The user's first name
 * @param lastName The user's last name
 * @param status Whether the requesting user and this user are friends. Can be null
 * @param initiated Whether the requesting client initiated the request. False if status is null
 * @param bio The user's bio
 * @param school The user's school
 * @param interests A list of this user's interests
 * @param hometown The user's hometown. Can be null
 * @param distance The distance, in km, between this user and the requesting client
 * @param grade The user's grade. Must be between 7 and 12, inclusive
 * @param gender The user's gender. Must be either M, F, O or X
 * @param matchScore The match score between this user and the requesting client. A value between 0 and 1, inclusive
 * @see me.rootatkali.hey.service.FriendService#match(User, User)
 * @see me.rootatkali.hey.service.FriendService
 */
public record FriendView(String id,
                         String username,
                         String firstName,
                         String lastName,
                         Friendship.Status status,
                         boolean initiated,
                         String bio,
                         School school,
                         List<Interest> interests,
                         String hometown,
                         double distance,
                         Integer grade,
                         Character gender,
                         double matchScore) {
}
