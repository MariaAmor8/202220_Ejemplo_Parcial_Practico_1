package co.edu.uniandes.dse.parcialejemplo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.parcialejemplo.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialejemplo.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ MedicoService.class, EspecialidadService.class, MedicoEspecialidadService.class })

public class MedicoEspecialidadServiceTest {

    @Autowired
    MedicoService medicoService;

    @Autowired
    EspecialidadService especialidadService;

    @Autowired
    MedicoEspecialidadService medicoEspecialidadService;

    @Autowired
    TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    @BeforeEach
    void setUp() {
        clearData();
    }

    public void clearData() {
        entityManager.getEntityManager().createQuery("delete from MedicoEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from EspecialidadEntity").executeUpdate();
    }

    @Test
    void crearMedicoTest() throws IllegalOperationException {
        MedicoEntity medicoPrueba = factory.manufacturePojo(MedicoEntity.class);
        medicoPrueba.setNombre("Jaime");
        medicoPrueba.setApellido("Moreno");
        medicoPrueba.setRegistroMedico("RM234");

        MedicoEntity result = medicoService.createMedico(medicoPrueba);
        assertNotNull(result);
        assertEquals(medicoPrueba.getNombre(), result.getNombre());
        assertEquals(medicoPrueba.getApellido(), result.getApellido());
        assertEquals(medicoPrueba.getRegistroMedico(), result.getRegistroMedico());
    }

    @Test
    void crearWrongMedicoTest() {
        assertThrows(IllegalOperationException.class, () -> {
            MedicoEntity medicoPrueba = factory.manufacturePojo(MedicoEntity.class);
            medicoPrueba.setNombre("Jaime");
            medicoPrueba.setApellido("Moreno");
            medicoPrueba.setRegistroMedico("AB45");
            medicoService.createMedico(medicoPrueba);
        });
    }

    @Test
    void crearEspecialidadTest() throws IllegalOperationException{
        EspecialidadEntity especialidadEntity = factory.manufacturePojo(EspecialidadEntity.class);
        especialidadEntity.setNombre("endocrinologia");
        especialidadEntity.setDescripcion("1234567891");
        EspecialidadEntity result = especialidadService.createEspecialidad(especialidadEntity);
        assertNotNull(result);
        assertEquals(especialidadEntity.getId(), result.getId());
        assertEquals(especialidadEntity.getNombre(), result.getNombre());
        assertEquals(especialidadEntity.getDescripcion(), result.getDescripcion());
    }

    @Test
    void crearEspecialidadNoValidaTest(){
        assertThrows(IllegalOperationException.class, () -> {
            EspecialidadEntity especialidadPrueba = factory.manufacturePojo(EspecialidadEntity.class);
            especialidadPrueba.setNombre("deportologo");
            especialidadPrueba.setDescripcion("123");
            especialidadService.createEspecialidad(especialidadPrueba);
        });
    }

    @Test
    void addEspecialidadTest() throws IllegalOperationException, EntityNotFoundException{
        MedicoEntity medicoEntity = factory.manufacturePojo(MedicoEntity.class);
        medicoEntity.setNombre("Jaime");
        medicoEntity.setApellido("Moreno");
        medicoEntity.setRegistroMedico("RM234");
        medicoService.createMedico(medicoEntity);

        EspecialidadEntity especialidadEntity =  factory.manufacturePojo(EspecialidadEntity.class);
        especialidadEntity.setNombre("cardiología");
        especialidadEntity.setDescripcion("12345678910");
        especialidadService.createEspecialidad(especialidadEntity);

        MedicoEntity result = medicoEspecialidadService.addEspecialidad(medicoEntity.getId(), especialidadEntity.getId());
        assertNotNull(result);
        EspecialidadEntity especialidadResult = result.getEspecialidades().get(0);
        assertEquals(especialidadEntity.getId(), especialidadResult.getId());
        assertEquals(especialidadEntity.getNombre(), especialidadResult.getNombre());
        assertEquals(especialidadEntity.getDescripcion(), especialidadResult.getDescripcion());
    }

    @Test
    void addEspialidadNoValida(){
        assertThrows(EntityNotFoundException.class, () -> {
            MedicoEntity medicoPrueba = factory.manufacturePojo(MedicoEntity.class);
            medicoPrueba.setNombre("Jaime");
            medicoPrueba.setApellido("Moreno");
            medicoPrueba.setRegistroMedico("RM45");
            medicoService.createMedico(medicoPrueba);

            medicoEspecialidadService.addEspecialidad(medicoPrueba.getId(), 0L);
        });
    }

    @Test
    void addEspecialidadMedicoNoValido(){
        assertThrows(EntityNotFoundException.class, () -> {
            EspecialidadEntity especialidadEntity = factory.manufacturePojo(EspecialidadEntity.class);
            especialidadEntity.setDescripcion("12345678912");
            especialidadEntity.setNombre("cardiologia");
            especialidadService.createEspecialidad(especialidadEntity);

            medicoEspecialidadService.addEspecialidad(0L, especialidadEntity.getId());
        });
    }


}
