package utils;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final String PERSISTENCE_UNIT_NAME = "DeliciasPU";
    private static EntityManagerFactory factory;

    static {
        try {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            System.out.println("✅ Conexión a la base de datos establecida correctamente.");
        } catch (Exception e) {
            System.err.println("❌ Error al establecer la conexión con la base de datos:");
            e.printStackTrace();
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return factory;
    }
}