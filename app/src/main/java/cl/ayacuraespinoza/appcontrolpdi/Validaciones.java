package cl.ayacuraespinoza.appcontrolpdi;

public class Validaciones {

    /**
     * Le da formato a un rut, concatenándole puntos y guión.
     * @param rut Rut a formatear.
     * @return Un nuevo String, con el rut formateado.
     */
    static public String formatoRut (String rut) {
        int contador = 0;
        String formato;
        if (rut.length() == 0) {
            return "";
        } else {
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            formato = "-" + rut.substring(rut.length() - 1);
            for (int i = rut.length() - 2; i >= 0; i--) {
                formato = rut.substring(i, i + 1) + formato;
                contador++;
                if (contador == 3 && i != 0) {
                    formato = "." + formato;
                    contador = 0;
                }
            }
            return formato;

        }
    }

        /**
         * Valida un rut de acuerdo a su dígito verificador.
         * @param rut Rut a validar
         * @return true si el rut es válido,
         * false en cualquier otro caso.
         */
    static public boolean validarRut(String rut){
        int suma=0;
        String dvRut,dvCalculado;
        int[] serie = {2,3,4,5,6,7};
        rut = rut.replace(".", "");
        rut = rut.replace("-", "");
        dvRut = rut.substring(rut.length()-1);
        for(int i = rut.length()-2;i>=0; i--){
            suma +=  Integer.valueOf(rut.substring(i, i+1))
                    *serie[(rut.length()-2-i)%6];
        }
        dvCalculado=String.valueOf(11-suma%11);
        if(dvCalculado.compareToIgnoreCase("10") == 0){
            dvCalculado = "K";
        }

        if(dvCalculado.compareToIgnoreCase(dvRut) == 0){
            return true;
        } else {
            return false;
        }
    }
}



