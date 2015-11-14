package ru.unn.agile.IntersectionOfSegments.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import ru.unn.agile.IntersectionOfSegments.model.Intersection;
import ru.unn.agile.IntersectionOfSegments.model.ParseException;
import ru.unn.agile.IntersectionOfSegments.model.Point;
import ru.unn.agile.IntersectionOfSegments.model.Segment;
import java.util.ArrayList;
import java.util.List;

public class ViewModel {
    private final StringProperty seg1Point1X = new SimpleStringProperty();
    private final StringProperty seg1Point1Y = new SimpleStringProperty();
    private final StringProperty seg1Point2X = new SimpleStringProperty();
    private final StringProperty seg1Point2Y = new SimpleStringProperty();

    private final StringProperty seg2Point1X = new SimpleStringProperty();
    private final StringProperty seg2Point1Y = new SimpleStringProperty();
    private final StringProperty seg2Point2X = new SimpleStringProperty();
    private final StringProperty seg2Point2Y = new SimpleStringProperty();

    private final BooleanProperty calculationDisabled = new SimpleBooleanProperty();

    private final StringProperty result = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();

    private final List<ValueChangeListener> valueChangedListeners = new ArrayList<>();

    public ViewModel() {
        seg1Point1X.set("");
        seg1Point1Y.set("");
        seg1Point2X.set("");
        seg1Point2Y.set("");

        seg2Point1X.set("");
        seg2Point1Y.set("");
        seg2Point2X.set("");
        seg2Point2Y.set("");

        result.set("");

        status.set(Status.WAITING.toString());

        BooleanBinding couldFind = new BooleanBinding() {
            {
                super.bind(seg1Point1X, seg1Point1Y, seg1Point2X, seg1Point2Y,
                        seg2Point1X, seg2Point1Y, seg2Point2X, seg2Point2Y);
            }
            @Override
            protected boolean computeValue() {
                return getInputStatus() == Status.READY;
            }
        };
        calculationDisabled.bind(couldFind.not());

        final List<StringProperty> fields = new ArrayList<StringProperty>() { {
            add(seg1Point1X);
            add(seg1Point1Y);
            add(seg1Point2X);
            add(seg1Point2Y);

            add(seg2Point1X);
            add(seg2Point1Y);
            add(seg2Point2X);
            add(seg2Point2Y);
        } };

        for (StringProperty field : fields) {
            final ValueChangeListener listener = new ValueChangeListener();
            field.addListener(listener);
            valueChangedListeners.add(listener);
        }
    }

    public void calculate() {
        Segment segment1 = new Segment(new Point(seg1Point1X.get(), seg1Point1Y.get()),
                new Point(seg1Point2X.get(), seg1Point2Y.get()));

        Segment segment2 = new Segment(new Point(seg2Point1X.get(), seg2Point1Y.get()),
                new Point(seg2Point2X.get(), seg2Point2Y.get()));

        setStringResult(segment1.isIntersectedWith(segment2));

        status.set(Status.SUCCESS.toString());
    }

    private void setStringResult(final Intersection intersection) {
        switch (intersection.getTypeOfIntersection()) {
            case NotIntersection:
                result.set("Segments not intersection.");
                break;
            case IntersectionInOnePoint:
                result.set("Segments intersection in one point "
                        + intersection.getSegment().getStart().toString()
                        + ".");
                break;
            case OnePartOfOther:
            case SegmentsHaveCommonPart:
                result.set("Segments have common part ["
                        + intersection.getSegment().getStart().toString()
                        + "; " + intersection.getSegment().getFinish().toString()
                        + "].\nLength = "
                        + Double.toString(intersection.getSegment().getLengthSegment()));
                break;
            default:
                break;
        }
    }

    public StringProperty seg1Point1XProperty() {
        return seg1Point1X;
    }

    public StringProperty seg1Point1YProperty() {
        return seg1Point1Y;
    }

    public StringProperty seg1Point2XProperty() {
        return seg1Point2X;
    }

    public StringProperty seg1Point2YProperty() {
        return seg1Point2Y;
    }

    public StringProperty seg2Point1XProperty() {
        return seg2Point1X;
    }

    public StringProperty seg2Point1YProperty() {
        return seg2Point1Y;
    }

    public StringProperty seg2Point2XProperty() {
        return seg2Point2X;
    }

    public StringProperty seg2Point2YProperty() {
        return seg2Point2Y;
    }

    public BooleanProperty calculationDisabledProperty() {
        return calculationDisabled;
    }

    public final boolean getCalculationDisabled() {
        return calculationDisabled.get();
    }

    public StringProperty resultProperty() {
        return result;
    }

    public final String getResult() {
        return result.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public final String getStatus() {
        return status.get();
    }

    private Status getInputStatus() {
        Status inputStatus = Status.READY;

        try {
            inputStatus = checkCorrectPoint(seg1Point1X, seg1Point1Y, inputStatus);
            inputStatus = checkCorrectPoint(seg1Point2X, seg1Point2Y, inputStatus);
            inputStatus = checkCorrectPoint(seg2Point1X, seg2Point1Y, inputStatus);
            inputStatus = checkCorrectPoint(seg2Point2X, seg2Point2Y, inputStatus);
        } catch (ParseException pe) {
            inputStatus = Status.BAD_FORMAT;
        }
        return inputStatus;
    }

    private Status checkCorrectPoint(final StringProperty x, final StringProperty y,
                                     final Status inputStatus) {
        try {
            new Point(x.get(), y.get());
        } catch (ParseException pe) {
            if (pe.getMessage().equals("Is empty argument")) {
                return Status.WAITING;
            } else {
                throw pe;
            }
        }
        return  inputStatus;
    }

    private class ValueChangeListener implements ChangeListener<String> {
        @Override
        public void changed(final ObservableValue<? extends String> observable,
                            final String oldValue, final String newValue) {
            status.set(getInputStatus().toString());
        }
    }
}

enum Status {
    WAITING("Please provide input data"),
    READY("Press 'Find of intersection' or Enter"),
    BAD_FORMAT("Bad format"),
    SUCCESS("Success");

    private final String name;
    private Status(final String name) {
        this.name = name;
    }
    public String toString() {
        return name;
    }
}
