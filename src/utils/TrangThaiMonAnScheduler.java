package utils;

import dao.MonAnDAO;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrangThaiMonAnScheduler {

    private MonAnDAO monAnDAO;

    public TrangThaiMonAnScheduler(MonAnDAO monAnDAO) {
        this.monAnDAO = monAnDAO;
    }

    public void start() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                monAnDAO.capNhatTrangThaiMonAnTheoNguyenLieu();
                System.out.println("Đã cập nhật trạng thái món ăn tự động.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.MINUTES); // Cập nhật mỗi 1 phút
    }
}
