package model;

public class MedicRecord {

    String patientName;
    String description;
    String medicacao;

    public MedicRecord(
            String patientName,
            String description,
            String medicacao
    ) {

        this.patientName = patientName;
        this.description = description;
        this.medicacao = medicacao;
    }

    @Override
    public String toString() {

        return "Paciente: " + patientName +
                " | Diagnostico: " + description +
                " | Medicacao: " + medicacao;
    }
}