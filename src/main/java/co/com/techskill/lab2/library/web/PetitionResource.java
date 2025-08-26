package co.com.techskill.lab2.library.web;

import co.com.techskill.lab2.library.domain.dto.PetitionDTO;
import co.com.techskill.lab2.library.service.IPetitionService;
import co.com.techskill.lab2.library.service.dummy.PetitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/petitions")
public class PetitionResource {
    private final IPetitionService petitionService;
    private final PetitionService petitionServiceDummy;

    public PetitionResource(IPetitionService petitionService, PetitionService petitionServiceDummy){
        this.petitionService = petitionService;
        this.petitionServiceDummy = petitionServiceDummy;
    }

    @GetMapping("/all")
    public Flux<PetitionDTO> getAllPetitions(){
        return petitionService.findALl();
    }

    @PostMapping("/id")
    public Mono<ResponseEntity<PetitionDTO>> findByPetitionId(@RequestBody PetitionDTO petitionDTO){
        return petitionService.findById(petitionDTO.getPetitionId())
                .map(ResponseEntity::ok);
    }

    @PostMapping("/id/intermitence")
    public Mono<ResponseEntity<String>> findByPetitionIdItermitence(@RequestBody PetitionDTO petitionDTO){
        return petitionService.simulateIntermittency(petitionDTO)
                .map(ResponseEntity::ok);

    }

    @PostMapping("/save")
    public Mono<ResponseEntity<PetitionDTO>> savePetition(@RequestBody PetitionDTO petitionDTO){
        return petitionService.save(petitionDTO)
                .map(ResponseEntity::ok);

    }

    @PostMapping("/revisar")
    public Flux<String> checkPetitions(@RequestBody PetitionDTO petitionDTO) {
        return petitionService.checkPriorities(petitionDTO);
    }

    @GetMapping("/dummy/check/flow")
    public Mono<ResponseEntity<String>> checkPetitionsFlow() {
        petitionServiceDummy.checkReturn().subscribe();
        return Mono.just(ResponseEntity.accepted().body("Flujo Iniciado, ver consola del servidor"));
    }
}
