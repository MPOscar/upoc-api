package rondanet.upoc.core.utils.fechas;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * The type Formatear fecha.
 */
@Component
public class FechaUtils {

    /**
     * Formatea fecha date time.
     *
     * @param formatoFecha    the formato fecha
     * @param fechaAFormatear the fecha a formatear
     * @return the date time
     */
    public DateTime formateaFecha(String formatoFecha, String fechaAFormatear){

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
        return formatter.parseDateTime(fechaAFormatear);
    }

    public Date sumarDiasAFecha(Date fecha, int dias){
        if (dias==0) return fecha;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
    }

    public static HashMap<String, DateTime> formatearFechas(String fechaIni, String fechaFin){
        DateTime fechaInicio = new DateTime();
        DateTime fechaFinal = fechaInicio;
        try {
            String[] fechaI = fechaIni.split("/");
            fechaIni = fechaI[1] + "/" + fechaI[0] + "/" + fechaI[2] + " 00:00:00";

            String[] fechaF = fechaFin.split("/");
            fechaFin = fechaF[1] + "/" + fechaF[0] + "/" + fechaF[2] + " 23:59:59";

            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
            fechaInicio = formatter.parseDateTime(fechaIni);
            fechaFinal = formatter.parseDateTime(fechaFin);

        } catch (Exception e) {
            //No hacer nada
        }
        HashMap<String, DateTime> fechas = new HashMap<>();
        fechas.put("fechaInicio", fechaInicio);
        fechas.put("fechaFinal", fechaFinal);
        return fechas;
    }

    public static DateTime obtenerPrimerDiaDelMes(DateTime dateTime)
    {
        dateTime = dateTime != null ? dateTime : new DateTime();
        DateTime firstDayOfMonth = dateTime
                .dayOfMonth().withMinimumValue()
                .hourOfDay().withMinimumValue()
                .minuteOfDay().withMinimumValue()
                .secondOfDay().withMinimumValue()
                .millisOfDay().withMinimumValue();
        return firstDayOfMonth;
    }

    public static DateTime obtenerUltimoMesDelAño(DateTime dateTime)
    {
        dateTime = dateTime != null ? dateTime : new DateTime();
        DateTime firstDayOfMonth = dateTime
                .monthOfYear().withMaximumValue()
                .dayOfYear().withMaximumValue();
        return firstDayOfMonth;
    }

    public static DateTime obtenerPrimerMesDelAño(DateTime dateTime)
    {
        dateTime = dateTime != null ? dateTime : new DateTime();
        DateTime firstDayOfMonth = dateTime
                .monthOfYear().withMinimumValue()
                .dayOfYear().withMinimumValue();
        return firstDayOfMonth;
    }

    public static DateTime obtenerUltimoDiaDelMes(DateTime dateTime)
    {
        dateTime = dateTime != null ? dateTime : new DateTime();
             DateTime lastDate = dateTime
                .dayOfMonth().withMaximumValue()
                .hourOfDay().withMaximumValue()
                .minuteOfDay().withMaximumValue()
                .secondOfDay().withMaximumValue()
                .millisOfDay().withMaximumValue();
        return lastDate;
    }

    public static DateTime obtenerDiaAnterior()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        DateTime lastDate = new DateTime(calendar.getTime())
                .hourOfDay().withMaximumValue()
                .minuteOfDay().withMaximumValue()
                .secondOfDay().withMaximumValue()
                .millisOfDay().withMaximumValue();
        return lastDate;
    }

    public static DateTime obtenerMesAnterior()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        DateTime lastDate = new DateTime(calendar.getTime())
                .dayOfMonth().withMaximumValue()
                .hourOfDay().withMaximumValue()
                .minuteOfDay().withMaximumValue()
                .secondOfDay().withMaximumValue()
                .millisOfDay().withMaximumValue();
        return lastDate;
    }

    public static boolean perteneceAlMesActual(DateTime fecha) {
        DateTime fechaActual = new DateTime();
        return fecha.getYear() == fechaActual.getYear() && fecha.getMonthOfYear() == fechaActual.getMonthOfYear();
    }

    public static boolean perteneceAlAnnoActual(DateTime fecha) {
        DateTime fechaActual = new DateTime();
        return fecha.getYear() == fechaActual.getYear();
    }

    public static DateTime obtenerFechaInicioDelMes(DateTime dateTime, int yearsToAdd)
    {
        dateTime = dateTime != null ? dateTime : new DateTime();
        int year = dateTime.getYear() + yearsToAdd;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        dateTime = formatter.parseDateTime("01/01/" + year + " 00:00:00");
        DateTime fechaInicio = dateTime
                .dayOfMonth().withMinimumValue()
                .hourOfDay().withMinimumValue()
                .minuteOfDay().withMinimumValue()
                .secondOfDay().withMinimumValue()
                .millisOfDay().withMinimumValue();
        return fechaInicio;
    }

    public static DateTime obtenerFechaInicio()
    {
        DateTime fechaInicio = new DateTime()
                .hourOfDay().withMinimumValue()
                .minuteOfDay().withMinimumValue()
                .secondOfDay().withMinimumValue()
                .millisOfDay().withMinimumValue();
        return fechaInicio;
    }

    public static DateTime obtenerFechaFinal()
    {
        DateTime fechaInicio = new DateTime()
                .hourOfDay().withMaximumValue()
                .minuteOfDay().withMaximumValue()
                .secondOfDay().withMaximumValue()
                .millisOfDay().withMaximumValue();
        return fechaInicio;
    }

}
