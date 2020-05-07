package com.elena.api;

import com.elena.model.Path;
import com.elena.model.Result;
import com.elena.service.ElenaService;
import com.elena.pathfinder.PathFinder;
import com.elena.pathfinder.PathFinderFactory;
import com.elena.utils.Algorithm;
import com.elena.utils.ElevationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping
@RestController
@CrossOrigin("*")
public class ElenaController {

    private final ElenaService elenaService;

    @Autowired
    public ElenaController(ElenaService elenaService) {
        this.elenaService = elenaService;
    }

    @GetMapping(path = "/autocomplete/{input}", produces = "application/json")
    public List<String> getAutoCompleteSuggestions(@PathVariable("input") String userInput) {
        return this.elenaService.getAutoCompleteSuggestions(userInput);
    }

    @GetMapping(path = "/find_path/{algorithm}", params = {"fromaddr", "toaddr", "number", "elemode", "tolerance"})
    public List<Result> findPathByAddress(@PathVariable("algorithm") String algorithmName,
                                          @RequestParam("fromaddr") String addressFrom,
                                          @RequestParam("toaddr") String addressTo,
                                          @RequestParam("number") int number,
                                          @RequestParam("elemode") String eleMode,
                                          @RequestParam("tolerance") int tolerance) {
        ElevationMode elevationMode = ElevationMode.valueOf(eleMode.toUpperCase());
        Algorithm algorithm = Algorithm.valueOf(algorithmName.toUpperCase());
        PathFinder pathFinder = PathFinderFactory.getPathFinder(algorithm, elevationMode, tolerance, number);

        List<Path> paths = this.elenaService.findPathByAddress(pathFinder, addressFrom, addressTo);

        return this.createResultSet(paths);
    }

    @GetMapping(path = "/find_path/{algorithm}", params = {"from", "to", "number", "elemode", "tolerance"})
    public List<Result> findPathByLonLat(@PathVariable("algorithm") String algorithmName,
                                         @RequestParam("from") String lonLatFrom,
                                         @RequestParam("to") String lonLatTo,
                                         @RequestParam("number") int number,
                                         @RequestParam("elemode") String eleMode,
                                         @RequestParam("tolerance") int tolerance) {
        ElevationMode elevationMode = ElevationMode.valueOf(eleMode.toUpperCase());
        Algorithm algorithm = Algorithm.valueOf(algorithmName.toUpperCase());
        PathFinder pathFinder = PathFinderFactory.getPathFinder(algorithm, elevationMode, tolerance, number);

        List<Path> paths = this.elenaService.findPathByLonLat(pathFinder, lonLatFrom, lonLatTo);

        return this.createResultSet(paths);
    }


    /**
     * Create a list of Result as a json format for frontend
     * @param paths list of shortest path
     * @return a list of Result objects
     */
    private List<Result> createResultSet(List<Path> paths) {
        return new ArrayList<Result>(){{
            paths.forEach(
                    (path) -> add(new Result(path))
            );
        }};
    }



}
