package in.naitik.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.naitik.entity.StudentEnqEntity;

public interface StudentEnqRepo extends JpaRepository<StudentEnqEntity, Integer>{

}
