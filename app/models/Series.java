package models;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import rpc.shared.data.Type;

@Entity
public class Series extends Model {
    private static final long serialVersionUID = 4994996456229543876L;

    public static Finder<Series> objects =
        new Finder<Series>(Series.class);

    @ManyToOne
    protected Indicator indicator;

    @ManyToOne
    protected Country country;

    @OneToMany
    protected List<Point> points;

    public Series() {
        super();
    }

    public Series(Indicator indicator, Country country) {
        this();
        this.indicator = indicator;
        this.country = country;
    }

    public Series(Indicator indicator, Country country, List<Point> points) {
        this(indicator, country);
        this.points = points;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public Country getCountry() {
        return country;
    }

    public List<Point> getPoints() {
        return points;
    }

    private Map<Integer, Point> pointsMap;

    public Map<Integer, Point> getPointsMap() {
        if (pointsMap == null) {
            pointsMap = new HashMap<Integer, Point>();

            for (Point point : points) {
                pointsMap.put(point.getYear(), point);
            }
        }
        return pointsMap;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public static String FIELD_COUNTRY = "country";
    public static String FIELD_POINTS = "points";

    @Override
    public Object get(String field) {
        if (field == FIELD_COUNTRY) return country;
        if (field == FIELD_POINTS) return points;
        return super.get(field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(String field, Object value) {
        super.set(field, value);
        if (field == FIELD_COUNTRY) country = (Country) value;
        if (field == FIELD_POINTS) points = (List<Point>) value;
    }

    private static Map<String, Type> fields;

    @Override
    public Map<String, Type> fields() {
        if (fields == null) {
            fields = super.fields();
            fields.put(FIELD_COUNTRY, Type.get(Country.class));
            fields.put(FIELD_POINTS, Type.get(
                List.class, Type.get(Point.class)));
        }
        return fields;
    }
}
