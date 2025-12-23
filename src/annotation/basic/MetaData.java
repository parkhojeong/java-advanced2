package annotation.basic;

@AnnoMeta
public class MetaData {

//    @AnnoMeta // X
    private String id;

    @AnnoMeta
    public void call() {

    }

    public static void main(String[] args) throws NoSuchMethodException {
        AnnoMeta annotation = MetaData.class.getAnnotation(AnnoMeta.class);
        System.out.println("annotation = " + annotation);

        AnnoMeta methodAnnotation = MetaData.class.getMethod("call").getAnnotation(AnnoMeta.class);
        System.out.println("methodAnnotation = " + methodAnnotation);
    }
}
