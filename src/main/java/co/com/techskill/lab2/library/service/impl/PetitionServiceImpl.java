package co.com.techskill.lab2.library.service.impl;

import co.com.techskill.lab2.library.config.PetitionMapper;
import co.com.techskill.lab2.library.config.PetitionMapperImpl;
import co.com.techskill.lab2.library.domain.dto.PetitionDTO;
import co.com.techskill.lab2.library.repository.IBookRepository;
import co.com.techskill.lab2.library.repository.IPetitionRepository;
import co.com.techskill.lab2.library.service.IPetitionService;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class PetitionServiceImpl implements IPetitionService {

    private final IPetitionRepository petitionRepository;
    private final IBookRepository bookRepository;
    private final PetitionMapper petitionMapper;

    public PetitionServiceImpl(IPetitionRepository petitionRepository, IBookRepository bookRepository) {
        this.petitionRepository = petitionRepository;
        this.bookRepository = bookRepository;
        this.petitionMapper = new PetitionMapperImpl();
    }
    @Override
    public Flux<PetitionDTO> findALl() {
        return petitionRepository
                .findAll()
                .map(petitionMapper::toDTO);
    }

    @Override
    public Mono<PetitionDTO> findById(String id) {
        return petitionRepository
                .findByPetitionId(id)
                .map(petitionMapper::toDTO);
    }

    @Override
    public Mono<PetitionDTO> save(PetitionDTO petitionDTO) {
        petitionDTO.setPetitionId(UUID.randomUUID().toString().substring(0,10));
        petitionDTO.setSentAt(LocalDate.now());
        return petitionRepository
                .save(petitionMapper.toEntity(petitionDTO))
                .map(petitionMapper::toDTO);
    }

    @Override
    public Flux<PetitionDTO> findByPriority(Integer p) {
        return petitionRepository.findAll()
                .filter(petition -> Objects.equals(petition.getPriority(), p))
                .map(petitionMapper::toDTO);
    }

    @Override
    public Flux<String> checkPriorities(PetitionDTO petitionDTO) {
        return findByPriority(petitionDTO.getPriority())
                .map(pt -> LocalTime.now() + " - Check priority with level: " + pt.getPriority()
                        + ", Petition ID: " + pt.getPetitionId()
                        + ", For book ID: " + pt.getBookId() + "\n")
                .delayElements(Duration.ofMillis(1000))
                .doOnNext(System.out::print);
    }

    //TO-DO: Simular una petición con tipo = LEND que falla si el libro asociado no está disponible.
    @Override
    public Flux<String> processPetition(PetitionDTO petitionDTO) {
        return null;
    }


    @Override
    public Mono<String> simulateIntermittency(PetitionDTO petitionDTO) {
        return null;

    }

}
