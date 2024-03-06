package co.edu.uniandes.dse.parcialejemplo.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialejemplo.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialejemplo.repositories.EspecialidadRepository;

@Service
public class EspecialidadService {
    
    @Autowired
    EspecialidadRepository especialidadRepository;

    @Transactional
    public EspecialidadEntity createEspecialidad(EspecialidadEntity especialidadEntity)throws IllegalOperationException{
        if(especialidadEntity.getDescripcion().length() < 10){
            throw new IllegalOperationException("La descripcion debe tener minimo 10 caracteres");
        }
        return especialidadRepository.save(especialidadEntity);
    }
}
