package co.edu.uniandes.dse.parcialejemplo.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialejemplo.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialejemplo.repositories.MedicoRepository;

@Service
public class MedicoService {
    
    @Autowired
    MedicoRepository medicoRepository;

    @Transactional
    public MedicoEntity createMedico(MedicoEntity medicoEntity)throws IllegalOperationException{
        String registro = medicoEntity.getRegistroMedico();
        String principio = registro.substring(0,2);
        if(!(principio.equals("RM"))){
            throw new IllegalOperationException("El registro debe empezar por 'RM'");
        }

        return medicoRepository.save(medicoEntity);
    }
}
