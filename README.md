
# Mancala Game 

 Below are the steps to use the API and some sample cURL requests for important endpoints.

## Steps to Play

1. **Create a User:** Before you can start a game, you need to create a user.

```bash
curl -X POST -H "Content-Type: application/json" -d '{"name":"John"}' http://localhost:8080/users/createUser
```
2.**Create a Game:** Once you have a user, you can start a new game.

```bash
curl -X POST -H "Content-Type: application/json" -d '{"userId":1}' http://localhost:8080/games/createGame
```

3.**Register User with a  Game:** Once you have created user, register with the game .

```bash
curl -X POST http://localhost:8080/register/1/users/1
```

4.**Start a Game:** Once you have a game and players registered, you can start it.

```bash
curl -X PUT http://localhost:8080/startGame/1
```


5. **Make a Move:** After starting a game, you can make a move by specifying the game ID and the pit index (1-14).

```bash
curl -X POST -H "Content-Type: application/json" -d '{"gameId":1, "playerId":1, "position":1}' http://localhost:8080/game/makeMove
```

6. **Get Game Status:** You can get the current status of a game by specifying the game ID.

```bash
curl -X GET http://localhost:8080/game/1
```

## Other Endpoints

- **Get All Game:** To get a list of all games, use the following command:

```bash
curl -X GET http://localhost:8080/games/all
```

- **List All Users:** To get a list of all users, use the following command:

```bash
curl -X GET http://localhost:8080/users/all
```
