import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class JpaExample {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        EntityManager em = emf.createEntityManager();

        try {
            String multiLineSqlQuery = "SELECT e "
                    + "FROM Employee e "
                    + "WHERE e.department = :dept "
                    + "AND e.salary >= :minSalary";

            Query query = em.createQuery(multiLineSqlQuery);
            query.setParameter("dept", "Sales");
            query.setParameter("minSalary", 50000);

            List<Employee> results = query.getResultList();

            for (Employee employee : results) {
                System.out.println("Employee Name: " + employee.getName());
            }
        } finally {
            em.close();
            emf.close();
        }
    }
}
