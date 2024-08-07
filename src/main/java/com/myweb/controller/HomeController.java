package com.myweb.controller;

import com.myweb.board.model.BoardDTO;
import com.myweb.board.service.BoardService;
import com.myweb.board.service.BoardServiceImpl;
import com.myweb.game.model.GameDTO;
import com.myweb.predict.service.PredictService;
import com.myweb.predict.service.PredictServiceImpl;
import com.myweb.team.model.SportDTO;
import com.myweb.team.service.SportService;
import com.myweb.team.service.SportServiceImpl;
import com.myweb.user.model.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.myweb.game.service.GameService;
import com.myweb.game.service.GameServiceImpl;
import com.myweb.user.service.UserService;
import com.myweb.user.service.UserServiceImpl;
import util.mybatis.MybatisUtil;

public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public HomeController() {
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doAction(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doAction(request, response);
	}

	protected void doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PredictService predictService = new PredictServiceImpl();
		predictService.predictUsers();

		UserService userService = new UserServiceImpl();
		List<UserDTO> userRank = userService.getUserRank(request, response);

		userService.updateGrade();

		GameService gameService = new GameServiceImpl();
		List<GameDTO> gameList = gameService.getGames(request, response);

		List<String> games = gameService.getGaming(gameList);
		List<GameDTO> soccerGames = new ArrayList<>();
		List<GameDTO> baseballGames = new ArrayList<>();
		List<GameDTO> basketballGames = new ArrayList<>();

		for (GameDTO gameDTO : gameList) {
			if (Integer.parseInt(gameDTO.getGameId()) < 101) {
				basketballGames.add(gameDTO);
			} else if (Integer.parseInt(gameDTO.getGameId()) < 201) {
				soccerGames.add(gameDTO);
			} else {
				baseballGames.add(gameDTO);
			}
		}

		System.out.println(games);

		SportService sportService = new SportServiceImpl();
		List<SportDTO> teamRank = sportService.getTeamRank(request, response);

		List<SportDTO> footballRank = new ArrayList<>();
		List<SportDTO> baseballRank = new ArrayList<>();
		List<SportDTO> basketballRank = new ArrayList<>();

		for (SportDTO sportDTO : teamRank) {
			String sport = sportDTO.getSports();

			switch (sport) {
			case "soccer":
				footballRank.add(sportDTO);
				break;
			case "baseball":
				baseballRank.add(sportDTO);
				break;
			case "basketball":
				if (sportDTO.getTeamName().contains("서울")) {
					sportDTO.setTeamName(sportDTO.getTeamName().substring(3, 5));
				} else {
					sportDTO.setTeamName(sportDTO.getTeamName().substring(0, 2));
				}
				basketballRank.add(sportDTO);
				break;
			}
		}

		request.setAttribute("userRank", userRank);
		request.setAttribute("footballRank", footballRank);
		request.setAttribute("baseballRank", baseballRank);
		request.setAttribute("basketballRank", basketballRank);

		request.setAttribute("soccerGames", soccerGames);
		request.setAttribute("baseballGames", baseballGames);
		request.setAttribute("basketballGames", basketballGames);
		request.setAttribute("games", games);

		
		
		BoardService boardService = new BoardServiceImpl(); 
		List<BoardDTO> homePosts = boardService.homePosts(request, response);
		
		List<BoardDTO> annPosts = new ArrayList<BoardDTO>();
		List<BoardDTO> popPosts = new ArrayList<BoardDTO>();
		
		for(BoardDTO postDTO : homePosts) {
			String category = postDTO.getCategory();
			if(category.equals("공지")) annPosts.add(postDTO);
			else popPosts.add(postDTO);
		}
		request.setAttribute("annPosts", annPosts);
		request.setAttribute("popPosts", popPosts);
		 

		request.getRequestDispatcher("main.jsp").forward(request, response);

	}
}
