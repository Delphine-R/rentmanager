package com.ensta.rentmanager.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.service.VehicleService;
import org.junit.Before;
import org.junit.Test;

import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VehicleServiceTest {
    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private VehicleDao vehicleDao;

    @Before
    public void setUp() {
        vehicleDao = mock(VehicleDao.class);
        vehicleService = new VehicleService(vehicleDao);
    }

    @Test
    public void testCreateVehicle() throws DaoException, ServiceException {
        Vehicle vehicle = new Vehicle(1, "Toyota", "Corolla", 5);
        when(vehicleDao.create(vehicle)).thenReturn(1L);

        long id = vehicleService.create(vehicle);
        assertEquals(1L, id);
    }

    @Test(expected = ServiceException.class)
    public void testCreateVehicle_InvalidData() throws DaoException, ServiceException {
        Vehicle vehicle = new Vehicle(1, "", "Corolla", -1);
        vehicleService.create(vehicle);
    }

    @Test
    public void testDeleteVehicleById() throws DaoException, ServiceException {
        int vehicleId = 1;
        vehicleService.deleteById(vehicleId);
        verify(vehicleDao, times(1)).deleteById(vehicleId);
    }

    @Test
    public void testFindVehicleById() throws DaoException, ServiceException {
        int vehicleId = 1;
        Vehicle vehicle = new Vehicle(vehicleId, "Toyota", "Corolla", 5);
        when(vehicleDao.findById(vehicleId)).thenReturn(vehicle);

        Vehicle foundVehicle = vehicleService.findById(vehicleId);
        assertEquals(vehicle, foundVehicle);
    }

    @Test
    public void testFindAllVehicles() throws DaoException, ServiceException {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Vehicle(1, "Toyota", "Corolla", 5));
        vehicles.add(new Vehicle(2, "Honda", "Accord", 4));
        when(vehicleDao.findAll()).thenReturn(vehicles);

        List<Vehicle> foundVehicles = vehicleService.findAll();
        assertEquals(vehicles.size(), foundVehicles.size());
        assertEquals(vehicles.get(0), foundVehicles.get(0));
        assertEquals(vehicles.get(1), foundVehicles.get(1));
    }

    @Test
    public void testCountVehicles() throws DaoException, ServiceException {
        int count = 5;
        when(vehicleDao.count()).thenReturn(count);

        int vehicleCount = vehicleService.count();
        assertEquals(count, vehicleCount);
    }
}
