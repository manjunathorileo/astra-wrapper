package com.orileo.attendance.wrapper.resource.astra;


import com.orileo.attendance.wrapper.entity.mssql.astra.Trans;
import com.orileo.attendance.wrapper.entity.mssql.astra.EmpMaster;
import com.orileo.attendance.wrapper.entity.mysql.astra.*;
import com.orileo.attendance.wrapper.repository.mssqlrepository.astra.TransRepository;
import com.orileo.attendance.wrapper.repository.mysqlrepository.astra.*;
import com.orileo.attendance.wrapper.service.astra.EmpMasterMssqlService;
import com.orileo.attendance.wrapper.service.astra.EmployeeAttendanceService;
import com.orileo.attendance.wrapper.util.DateUtil;
import org.hibernate.cfg.annotations.reflection.PersistentAttributeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.orileo.attendance.wrapper.util.DateUtil.convertToDate;
import static com.orileo.attendance.wrapper.util.DateUtil.getDoubleDigits;

@RestController
@Configuration
@EnableScheduling
public class AstraController {

    @Autowired
    EmpMasterMssqlService empMasterMssqlService;
    @Autowired
    TransRepository transRepository;
    @Autowired
    PermanentContractRepo permanentContractRepo;
    @Autowired
    PermanentContractAttendanceRepo permanentContractAttendanceRepo;
    @Autowired
    InCsvRawRepository inCsvRawRepository;
    @Autowired
    VisitorPassRepository visitorPassRepository;
    @Autowired
    OutCsvRawRepository outCsvRawRepository;
    @Autowired
    EmployeeAttendanceService employeeAttendanceService;
    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * Sync Emp Table from MSSQL to MYSQL
     *
     * @return
     */
    @GetMapping("vivo/emp")
//    @Scheduled(initialDelay = 1000, fixedRate = 3600000)
    public List<EmpMaster> getEntriesFromGebe() {
        List<EmpMaster> empMasters = empMasterMssqlService.getEmpEntries();
        for (EmpMaster empMaster : empMasters) {
            EmpPermanentContract empPermanentContract = permanentContractRepo.findByEmployeeCode(empMaster.getEmpId());
            if (empPermanentContract == null) {
                EmpPermanentContract empPermanentContractNew = new EmpPermanentContract();
                empPermanentContractNew.setEmployeeCode(empMaster.getEmpId());
                empPermanentContractNew.setCardId(empMaster.getCardId());
                empPermanentContractNew.setFirstName(empMaster.getEmpname());
                empPermanentContractNew.setEmployeeType(EmployeeType.CONTRACT);
                empPermanentContractNew.setSafetyVest(false);
                empPermanentContractNew.setStatus(true);
                permanentContractRepo.save(empPermanentContractNew);
            } else {
                System.out.println(empMaster.getCardId() + " already synced");
            }
        }
        System.out.println("Size: " + empMasters.size());
        return empMasters;
    }

    @GetMapping("vivo/sync-trans")
//    @Scheduled(initialDelay = 1000, fixedRate = 1050)
    public List<Trans> getTransEntries() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String d = simpleDateFormat.format(new Date());
        Date date = simpleDateFormat.parse(d);
        List<Trans> transList = transRepository.findByLoggedOn(date);
        for (Trans trans : transList) {
            System.out.println("emcode--" + trans.getEmployeeCode());
            EmpPermanentContract empPermanentContract = permanentContractRepo.findByEmployeeCode(trans.getEmployeeCode());
            if (empPermanentContract != null) {
                if (!trans.getEmployeeCode().equalsIgnoreCase("")) {
                    PermanentContractAttendance permanentContractAttendance = permanentContractAttendanceRepo.getEmployeeAttendanceByEmployeeCode(trans.getLoggedOn(), trans.getEmployeeCode());
                    if (permanentContractAttendance == null) {
                        PermanentContractAttendance permanentContractAttendanceNew = new PermanentContractAttendance();
                        if (trans.getInOut().equals("01")) {
                            permanentContractAttendanceNew.setEmployeeCode(trans.getEmployeeCode());
                            permanentContractAttendanceNew.setAttendanceStatus(AttendanceStatus.PRESENT);
                            permanentContractAttendanceNew.setEmployeeName(trans.getEmployeeName());
                            permanentContractAttendanceNew.setInTime(trans.getEntryTime());
                            permanentContractAttendanceNew.setMarkedOn(trans.getLoggedOn());
                            permanentContractAttendanceRepo.save(permanentContractAttendanceNew);
                            System.out.println("IN DONE");
                        }
                    } else if (permanentContractAttendance != null) {
                        if (trans.getInOut().equals("02")) {
                            permanentContractAttendance.setOutTime(trans.getEntryTime());
                            long secs = (permanentContractAttendance.getInTime().getTime() - trans.getEntryTime().getTime()) / 1000;
                            long hours = secs / 3600;
                            long h = Math.abs(hours);
                            permanentContractAttendance.setWorkedHours(String.valueOf(h));
                            permanentContractAttendanceRepo.save(permanentContractAttendance);
                            System.out.println("OUT DONE");
                        }
                    } else {
                        System.out.println("y here");
                    }
                }
            }

        }
        return transList;
    }


    public List<Trans> getTransEntriesPermanentEmployee() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String d = simpleDateFormat.format(new Date());
        Date date = simpleDateFormat.parse(d);
        List<Trans> transList = transRepository.findByLoggedOn(date);//and type
        for (Trans trans : transList) {
            Employee employee = employeeRepository.findByEmployeeCode(trans.getEmployeeCode());
            EmployeeAttendance permanentContractAttendance = employeeAttendanceService.getTodayMarkedEmployeeAttendance(trans.getLoggedOn(), employee);
            if (permanentContractAttendance == null) {
                PermanentContractAttendance permanentContractAttendanceNew = new PermanentContractAttendance();
                if (trans.getInOut().equals("01")) {
                    permanentContractAttendanceNew.setEmployeeCode(trans.getEmployeeCode());
                    permanentContractAttendanceNew.setAttendanceStatus(AttendanceStatus.PRESENT);
                    permanentContractAttendanceNew.setEmployeeName(trans.getEmployeeName());
                    permanentContractAttendanceNew.setInTime(trans.getEntryTime());
                    permanentContractAttendanceNew.setMarkedOn(trans.getLoggedOn());
                    permanentContractAttendanceRepo.save(permanentContractAttendanceNew);
                    System.out.println("IN DONE");
                }
            } else if (permanentContractAttendance != null) {
                if (trans.getInOut().equals("02")) {
                    permanentContractAttendance.setOutTime(trans.getEntryTime());
                    long secs = (permanentContractAttendance.getInTime().getTime() - trans.getEntryTime().getTime()) / 1000;
                    long hours = secs / 3600;
                    long h = Math.abs(hours);
                    permanentContractAttendance.setWorkedHours(String.valueOf(h));
                    employeeAttendanceService.createEmployeeAttendance(permanentContractAttendance);
                    System.out.println("OUT DONE");
                }

            } else {
                System.out.println("y here");
            }
        }
        return transList;
    }


    /**
     * Write
     */
    @GetMapping("save-emp-from-mysql-to-mssql")
    @Scheduled(initialDelay = 1001, fixedRate = 3600000)
    public void saveEmp() {
        List<EmpPermanentContract> empPermanentContracts = permanentContractRepo.findAll();
        for (EmpPermanentContract empPermanentContract : empPermanentContracts) {
            EmpMaster empMaster = new EmpMaster();
            empMaster.setCardId(empPermanentContract.getCardId());
            empMaster.setEmpId(empPermanentContract.getEmployeeCode());
            empMaster.setEmpname(empPermanentContract.getFirstName());
            System.out.println("Which employee "+empPermanentContract.getEmployeeCode());
            EmpMaster v = empMasterMssqlService.getEmp(empMaster.getEmpId());
            if (v == null) {
                empMasterMssqlService.save(empMaster);
            } else {
                System.out.println("emp already exists");
                empMaster = v;
                empMaster.setCardId(empPermanentContract.getCardId());
                empMaster.setEmpId(empPermanentContract.getEmployeeCode());
                empMaster.setEmpname(empPermanentContract.getFirstName());
                empMasterMssqlService.save(empMaster);
            }
        }
        List<VisitorPass> visitorPasses = visitorPassRepository.findAll();
        for (VisitorPass visitorPass : visitorPasses) {
            EmpMaster empMaster = new EmpMaster();
            empMaster.setCardId(visitorPass.getRfid());
            empMaster.setEmpId(visitorPass.getMobileNumber());
            empMaster.setEmpname(visitorPass.getFirstName());
            EmpMaster v = empMasterMssqlService.getEmp(empMaster.getEmpId());
            if (v == null) {
                empMasterMssqlService.save(empMaster);
            } else {
                empMaster = v;
                empMaster.setCardId(visitorPass.getRfid());
                empMaster.setEmpId(visitorPass.getMobileNumber());
                empMaster.setEmpname(visitorPass.getFirstName());
                empMasterMssqlService.save(empMaster);
                System.out.println("vis already exists");
            }
        }
    }


    @GetMapping("astra/push-in-punches")
    @Scheduled(initialDelay = 1200, fixedRate = 5000)
    public List<Trans> getTransEntriesOfFewDays() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String d = simpleDateFormat.format(new Date());
        Date date = simpleDateFormat.parse(d);
        List<Trans> transList = transRepository.findByLoggedOn(date);
        for (Trans trans : transList) {
            if (!trans.getEmployeeCode().equalsIgnoreCase("")) {
                EmpPermanentContract empPermanentContract = permanentContractRepo.findByEmployeeCode(trans.getEmployeeCode());
                if (empPermanentContract != null) {
                    System.out.println("emcode--" + trans.getEmployeeCode() + "inout "+trans.getInOut());
                    if (trans.getInOut().equals("01")) {
                        SimpleDateFormat simpleDateFormatFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String entry = simpleDateFormatFull.format(trans.getEntryTime());
                        Date entrydate = simpleDateFormatFull.parse(entry);
                        InCsvRaw inCsvRaw = inCsvRawRepository.findByLogId(trans.getId());
                        if (inCsvRaw == null) {
                            System.out.println("Punching in access date :" + trans.getEntryTime());
                            InCsvRaw inCsvRawNew = new InCsvRaw();
                            inCsvRawNew.setAccessDate(trans.getEntryTime());
                            inCsvRawNew.setBId(trans.getCardId());
                            inCsvRawNew.setSsoId(trans.getEmployeeCode());
                            inCsvRawNew.setName(trans.getEmployeeName());
                            inCsvRawNew.setCId(trans.getGateNo());
                            inCsvRawNew.setLogId(trans.getId());
                            if (empPermanentContract.getEmployeeType().equals(EmployeeType.PERMANENT_CONTRACT)) {
                                inCsvRawNew.setType("Employee");
                            } else if (empPermanentContract.getEmployeeType().equals(EmployeeType.CONTRACT)) {
                                inCsvRawNew.setType("Contractor");
                            }
                            inCsvRawRepository.save(inCsvRawNew);
                        } else {
                            System.out.println("Punch exists In");
                        }
                    } else if (trans.getInOut().equals("02")) {
                        System.out.println("Punching out");
                        SimpleDateFormat simpleDateFormatFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String entry = simpleDateFormatFull.format(trans.getEntryTime());
                        Date entrydate = simpleDateFormatFull.parse(entry);
                        OutCsvRaw outCsvRaw = outCsvRawRepository.findByLogId(trans.getId());
                        if (outCsvRaw == null) {
                            OutCsvRaw outCsvRawNew = new OutCsvRaw();
                            outCsvRawNew.setAccessDate(trans.getEntryTime());
                            outCsvRawNew.setBId(trans.getCardId());
                            outCsvRawNew.setSsoId(trans.getEmployeeCode());
                            outCsvRawNew.setName(trans.getEmployeeName());
                            outCsvRawNew.setCId(trans.getGateNo());
                            outCsvRawNew.setLogId(trans.getId());
                            if (empPermanentContract.getEmployeeType().equals(EmployeeType.PERMANENT_CONTRACT)) {
                                outCsvRawNew.setType("Employee");
                            } else if (empPermanentContract.getEmployeeType().equals(EmployeeType.CONTRACT)) {
                                outCsvRawNew.setType("Contractor");
                            }
                            outCsvRawRepository.save(outCsvRawNew);
                        } else {
                            System.out.println("Punch exists Out");
                        }
                    }

                } else {

                    VisitorPass visitorPass = visitorPassRepository.findByMobileNumber(trans.getEmployeeCode());
                    if (visitorPass != null) {

                        if (trans.getInOut().equals("01")) {
                            SimpleDateFormat simpleDateFormatFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            String entry = simpleDateFormatFull.format(trans.getEntryTime());
                            Date entrydate = simpleDateFormatFull.parse(entry);
                            InCsvRaw inCsvRaw = inCsvRawRepository.findByLogId(trans.getId());
                            if (inCsvRaw == null) {
                                InCsvRaw inCsvRawNew = new InCsvRaw();
                                inCsvRawNew.setAccessDate(trans.getEntryTime());
                                inCsvRawNew.setBId(trans.getCardId());
                                inCsvRawNew.setSsoId(trans.getEmployeeCode());
                                inCsvRawNew.setName(trans.getEmployeeName());
                                inCsvRawNew.setCId(trans.getGateNo());
                                inCsvRawNew.setLogId(trans.getId());
                                if (visitorPass.getVisitType() != null) {
                                    if (visitorPass.getVisitType().equalsIgnoreCase("Temporary")) {
                                        inCsvRawNew.setType("Temp");
                                    } else if (visitorPass.getVisitType().equalsIgnoreCase("Visitor")) {
                                        inCsvRawNew.setType(visitorPass.getVisitType());
                                    } else {
                                        inCsvRawNew.setType("Visitor");
                                    }
                                } else {
                                    inCsvRawNew.setType("Visitor");
                                }
                                inCsvRawRepository.save(inCsvRawNew);
                            } else {
                                System.out.println("Already Vis logged on this date: " + trans.getEntryTime());
                            }
                        } else if (trans.getInOut().equals("02")) {
                            SimpleDateFormat simpleDateFormatFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            String entry = simpleDateFormatFull.format(trans.getEntryTime());
                            Date entrydate = simpleDateFormatFull.parse(entry);
                            OutCsvRaw outCsvRaw = outCsvRawRepository.findByLogId(trans.getId());
                            if (outCsvRaw == null) {
                                OutCsvRaw outCsvRawNew = new OutCsvRaw();
                                outCsvRawNew.setAccessDate(trans.getEntryTime());
                                outCsvRawNew.setBId(trans.getCardId());
                                outCsvRawNew.setSsoId(trans.getEmployeeCode());
                                outCsvRawNew.setName(trans.getEmployeeName());
                                outCsvRawNew.setCId(trans.getGateNo());
                                outCsvRawNew.setLogId(trans.getId());
                                if (visitorPass.getVisitType() != null) {
                                    if (visitorPass.getVisitType().equalsIgnoreCase("Temporary")) {
                                        outCsvRawNew.setType("Temp");
                                    } else if (visitorPass.getVisitType().equalsIgnoreCase("Visitor")) {
                                        outCsvRawNew.setType(visitorPass.getVisitType());
                                    } else {
                                        outCsvRawNew.setType("Visitor");
                                    }
                                } else {
                                    outCsvRawNew.setType("Visitor");
                                }
                                outCsvRawRepository.save(outCsvRawNew);
                            } else {
                                System.out.println("Already Vis logged on this date: " + trans.getEntryTime());
                            }
                        }

                    }
                }
            }
        }
        return transList;
    }



    @GetMapping("astra/push-punches-previous")
    public void realAutoSync() throws ParseException {
        Date startDate = getStartDateNumberOfMonths(2);
        Date endDate = new Date();
        List<Date> dates = getDaysBetweenDates(startDate, endDate);
        for (Date date : dates){
            getTransEntriesOfFewDaysPrev(date);
        }
    }


    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate) {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        Date currentDate = null;
        String dateStr = "";
        String day = "";
        String month = "";
        int year = 0;

        while (calendar.getTime().before(enddate)) {
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            day = getDoubleDigits(dd);

            int mm = calendar.get(Calendar.MONTH) + 1;
            month = getDoubleDigits(mm);

            year = calendar.get(Calendar.YEAR);
            dateStr = day + "/" + month + "/" + year;
            currentDate = convertToDate(dateStr);
            dates.add(currentDate);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

//    @Scheduled(initialDelay = 1000, fixedRate = 1050)
    public List<Trans> getTransEntriesOfFewDaysPrev(Date dateOn) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String d = simpleDateFormat.format(dateOn);
        Date date = simpleDateFormat.parse(d);
        List<Trans> transList = transRepository.findByLoggedOn(date);
        for (Trans trans : transList) {
            if (!trans.getEmployeeCode().equalsIgnoreCase("")) {
                EmpPermanentContract empPermanentContract = permanentContractRepo.findByEmployeeCode(trans.getEmployeeCode());
                if (empPermanentContract != null) {
                    System.out.println("emcode--" + trans.getEmployeeCode());
                    if (trans.getInOut().equals("01")) {
                        SimpleDateFormat simpleDateFormatFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String entry = simpleDateFormatFull.format(trans.getEntryTime());
                        Date entrydate = simpleDateFormatFull.parse(entry);
                        InCsvRaw inCsvRaw = inCsvRawRepository.findByLogId(trans.getId());
                        if (inCsvRaw == null) {
                            InCsvRaw inCsvRawNew = new InCsvRaw();
                            inCsvRawNew.setAccessDate(trans.getEntryTime());
                            inCsvRawNew.setBId(trans.getCardId());
                            inCsvRawNew.setSsoId(trans.getEmployeeCode());
                            inCsvRawNew.setName(trans.getEmployeeName());
                            inCsvRawNew.setCId(trans.getGateNo());
                            inCsvRawNew.setLogId(trans.getId());
                            if (empPermanentContract.getEmployeeType().equals(EmployeeType.PERMANENT_CONTRACT)) {
                                inCsvRawNew.setType("Employee");
                            } else if (empPermanentContract.getEmployeeType().equals(EmployeeType.CONTRACT)) {
                                inCsvRawNew.setType("Contractor");
                            }
                            inCsvRawRepository.save(inCsvRawNew);
                        } else {
                            System.out.println("Punch exists In");
                        }
                    } else if (trans.getInOut().equals("02")) {
                        SimpleDateFormat simpleDateFormatFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String entry = simpleDateFormatFull.format(trans.getEntryTime());
                        Date entrydate = simpleDateFormatFull.parse(entry);
                        OutCsvRaw outCsvRaw = outCsvRawRepository.findByLogId(trans.getId());
                        if (outCsvRaw == null) {
                            OutCsvRaw outCsvRawNew = new OutCsvRaw();
                            outCsvRawNew.setAccessDate(trans.getEntryTime());
                            outCsvRawNew.setBId(trans.getCardId());
                            outCsvRawNew.setSsoId(trans.getEmployeeCode());
                            outCsvRawNew.setName(trans.getEmployeeName());
                            outCsvRawNew.setCId(trans.getGateNo());
                            outCsvRawNew.setLogId(trans.getId());
                            if (empPermanentContract.getEmployeeType().equals(EmployeeType.PERMANENT_CONTRACT)) {
                                outCsvRawNew.setType("Employee");
                            } else if (empPermanentContract.getEmployeeType().equals(EmployeeType.CONTRACT)) {
                                outCsvRawNew.setType("Contractor");
                            }
                            outCsvRawRepository.save(outCsvRawNew);
                        } else {
                            System.out.println("Punch exists Out");
                        }
                    }

                } else {

                    VisitorPass visitorPass = visitorPassRepository.findByMobileNumber(trans.getEmployeeCode());
                    if (visitorPass != null) {

                        if (trans.getInOut().equals("01")) {
                            SimpleDateFormat simpleDateFormatFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            String entry = simpleDateFormatFull.format(trans.getEntryTime());
                            Date entrydate = simpleDateFormatFull.parse(entry);
                            InCsvRaw inCsvRaw = inCsvRawRepository.findByLogId(trans.getId());
                            if (inCsvRaw == null) {
                                InCsvRaw inCsvRawNew = new InCsvRaw();
                                inCsvRawNew.setAccessDate(trans.getEntryTime());
                                inCsvRawNew.setBId(trans.getCardId());
                                inCsvRawNew.setSsoId(trans.getEmployeeCode());
                                inCsvRawNew.setName(trans.getEmployeeName());
                                inCsvRawNew.setCId(trans.getGateNo());
                                inCsvRawNew.setLogId(trans.getId());
                                if (visitorPass.getVisitType() != null) {
                                    if (visitorPass.getVisitType().equalsIgnoreCase("Temporary")) {
                                        inCsvRawNew.setType("Temp");
                                    } else if (visitorPass.getVisitType().equalsIgnoreCase("Visitor")) {
                                        inCsvRawNew.setType(visitorPass.getVisitType());
                                    } else {
                                        inCsvRawNew.setType("Visitor");
                                    }
                                } else {
                                    inCsvRawNew.setType("Visitor");
                                }
                                inCsvRawRepository.save(inCsvRawNew);
                            } else {
                                System.out.println("Already Vis logged on this date: " + trans.getEntryTime());
                            }
                        } else if (trans.getInOut().equals("02")) {
                            SimpleDateFormat simpleDateFormatFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            String entry = simpleDateFormatFull.format(trans.getEntryTime());
                            Date entrydate = simpleDateFormatFull.parse(entry);
                            OutCsvRaw outCsvRaw = outCsvRawRepository.findByLogId(trans.getId());
                            if (outCsvRaw == null) {
                                OutCsvRaw outCsvRawNew = new OutCsvRaw();
                                outCsvRawNew.setAccessDate(trans.getEntryTime());
                                outCsvRawNew.setBId(trans.getCardId());
                                outCsvRawNew.setSsoId(trans.getEmployeeCode());
                                outCsvRawNew.setName(trans.getEmployeeName());
                                outCsvRawNew.setCId(trans.getGateNo());
                                outCsvRawNew.setLogId(trans.getId());
                                if (visitorPass.getVisitType() != null) {
                                    if (visitorPass.getVisitType().equalsIgnoreCase("Temporary")) {
                                        outCsvRawNew.setType("Temp");
                                    } else if (visitorPass.getVisitType().equalsIgnoreCase("Visitor")) {
                                        outCsvRawNew.setType(visitorPass.getVisitType());
                                    } else {
                                        outCsvRawNew.setType("Visitor");
                                    }
                                } else {
                                    outCsvRawNew.setType("Visitor");
                                }
                                outCsvRawRepository.save(outCsvRawNew);
                            } else {
                                System.out.println("Already Vis logged on this date: " + trans.getEntryTime());
                            }
                        }

                    }
                }
            }
        }
        return transList;
    }

    @GetMapping("whats-date")
    public Date getStartDateNumberOfMonths(long number) {
        Date date = new Date();
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(number);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date syncFrom = Date.from(earlier.atStartOfDay(defaultZoneId).toInstant());
        System.out.println(syncFrom);
        return syncFrom;
    }
}


