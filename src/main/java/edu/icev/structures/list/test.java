package edu.icev.structures.list;

public class test {
    public static void main(String[] args) {
                LinkedList<String> estados = new LinkedList<String>();

        estados.add("PI");
        estados.add("SP");
        estados.add("RJ");
        estados.add("CE");
        System.out.println(estados.get(0));

        System.out.println("Tamanho: " + estados.getSize());
        for(int i=0; i < estados.getSize(); i++){
            System.out.println(estados.get(i).getValue());
        }

        System.out.println("Removendo SP...");
        estados.remove("SP");


        System.out.println("Tamanho: " + estados.getSize());
        for(int i=0; i < estados.getSize(); i++){
            System.out.println(estados.get(i).getValue());
        }

        System.out.println();
        System.out.println("Listando DDD");
        System.out.println();

        LinkedList<Integer> ddd = new LinkedList<Integer>();
        ddd.add(89);
        ddd.add(86);
        ddd.add(11);

        System.out.println("Tamanho: " + ddd.getSize());
        for(int i=0; i < ddd.getSize(); i++){
            System.out.println(ddd.get(i).getValue());
        }

    }
}
