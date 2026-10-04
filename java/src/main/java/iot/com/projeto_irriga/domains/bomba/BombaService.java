package iot.com.projeto_irriga.domains.bomba;


import org.springframework.stereotype.Service;

@Service
public class BombaService {

    private final BombaRepository bombaRepository;
    public BombaService(BombaRepository bombaRepository){
        this.bombaRepository = bombaRepository;
    }


    public BombaEntity getFirstBombaEntity(){
        BombaEntity bomba = this.bombaRepository.findFirstByOrderByIdAsc();

        return  bomba;
    }

}
