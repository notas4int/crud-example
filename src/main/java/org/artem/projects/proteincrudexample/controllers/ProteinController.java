package org.artem.projects.proteincrudexample.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.artem.projects.proteincrudexample.dtos.ProteinCreatingDTO;
import org.artem.projects.proteincrudexample.entities.Protein;
import org.artem.projects.proteincrudexample.exceptions.ExceptionResponse;
import org.artem.projects.proteincrudexample.services.ProteinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ProteinController", description = " - manages requests to receive, add, update, and delete protein")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/protein")
public class ProteinController {
    private final ProteinService proteinService;

    @Operation(
            summary = "Retrieve protein by id",
            description = "Get protein objects by id. The response includes the protein's name, brand, and cost."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns the protein", content = @Content(schema = @Schema(implementation = Protein.class))),
            @ApiResponse(responseCode = "404", description = "Protein not found" , content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/getProtein/{id}")
    public ResponseEntity<Protein> getProtein(@PathVariable long id) {
        return ResponseEntity.ok(proteinService.findProteinById(id));
    }

    @Operation(
            summary = "Create protein info by protein entered object",
            description = "Create protein info objects by protein entered object. The request includes the protein's id, name, brand, and cost."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful creation of the protein", content = @Content(schema = @Schema(implementation = Protein.class))),
            @ApiResponse(responseCode = "400", description = "Protein isn't created" ,  content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/createProtein")
    public ResponseEntity<Protein> createProtein(@RequestBody ProteinCreatingDTO protein) {
        return ResponseEntity.ok(proteinService.saveProtein(protein));
    }

    @Operation(
            summary = "Update protein info by protein entered object",
            description = "Update protein info objects by protein entered object. The request includes the protein's id, name, brand, and cost."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful update of the protein", content = @Content(schema = @Schema(implementation = Protein.class))),
            @ApiResponse(responseCode = "400", description = "Protein isn't updated" , content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/updateProtein")
    public ResponseEntity<Protein> updateProtein(@RequestBody Protein protein) {
        return ResponseEntity.ok(proteinService.updateProtein(protein));
    }

    @Operation(
            summary = "Delete protein object by id",
            description = "Delete protein object by id. If the id is not found, there will be an exception."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful deletion of the protein"),
            @ApiResponse(responseCode = "400", description = "Protein isn't deleted" , content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/deleteProtein/{id}")
    public ResponseEntity<HttpStatus> deleteProtein(@PathVariable long id) {
        proteinService.deleteProteinById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
