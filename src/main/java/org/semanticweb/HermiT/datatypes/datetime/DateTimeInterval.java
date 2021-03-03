package org.semanticweb.HermiT.datatypes.datetime;

import java.util.Collection;

public class DateTimeInterval {
    protected final IntervalType m_intervalType;
    protected final long m_lowerBound;
    protected final BoundType m_lowerBoundType;
    protected final long m_upperBound;
    protected final BoundType m_upperBoundType;

    public DateTimeInterval(IntervalType intervalType, long lowerBound, BoundType lowerBoundType, long upperBound, BoundType upperBoundType) {
        assert (!DateTimeInterval.isIntervalEmpty(lowerBound, lowerBoundType, upperBound, upperBoundType));
        this.m_intervalType = intervalType;
        this.m_lowerBound = lowerBound;
        this.m_lowerBoundType = lowerBoundType;
        this.m_upperBound = upperBound;
        this.m_upperBoundType = upperBoundType;
    }

    public DateTimeInterval intersectWith(DateTimeInterval that) {
        BoundType newUpperBoundType;
        BoundType newLowerBoundType;
        long newUpperBound;
        long newLowerBound;
        if (this.m_intervalType != that.m_intervalType) {
            return null;
        }
        if (this.m_lowerBound < that.m_lowerBound) {
            newLowerBound = that.m_lowerBound;
            newLowerBoundType = that.m_lowerBoundType;
        } else if (this.m_lowerBound > that.m_lowerBound) {
            newLowerBound = this.m_lowerBound;
            newLowerBoundType = this.m_lowerBoundType;
        } else {
            newLowerBound = this.m_lowerBound;
            newLowerBoundType = BoundType.getMoreRestrictive(this.m_lowerBoundType, that.m_lowerBoundType);
        }
        if (this.m_upperBound < that.m_upperBound) {
            newUpperBound = this.m_upperBound;
            newUpperBoundType = this.m_upperBoundType;
        } else if (this.m_upperBound > that.m_upperBound) {
            newUpperBound = that.m_upperBound;
            newUpperBoundType = that.m_upperBoundType;
        } else {
            newUpperBound = this.m_upperBound;
            newUpperBoundType = BoundType.getMoreRestrictive(this.m_upperBoundType, that.m_upperBoundType);
        }
        if (DateTimeInterval.isIntervalEmpty(newLowerBound, newLowerBoundType, newUpperBound, newUpperBoundType)) {
            return null;
        }
        if (this.isEqual(this.m_intervalType, newLowerBound, newLowerBoundType, newUpperBound, newUpperBoundType)) {
            return this;
        }
        if (that.isEqual(this.m_intervalType, newLowerBound, newLowerBoundType, newUpperBound, newUpperBoundType)) {
            return that;
        }
        return new DateTimeInterval(this.m_intervalType, newLowerBound, newLowerBoundType, newUpperBound, newUpperBoundType);
    }

    protected boolean isEqual(IntervalType intervalType, long lowerBound, BoundType lowerBoundType, long upperBound, BoundType upperBoundType) {
        return this.m_intervalType == intervalType && this.m_lowerBound == lowerBound && this.m_lowerBoundType == lowerBoundType && this.m_upperBound == upperBound && this.m_upperBoundType == upperBoundType;
    }

    public int subtractSizeFrom(int argument) {
        if (argument <= 0) {
            return 0;
        }
        if (this.m_lowerBound < this.m_upperBound) {
            return 0;
        }
        assert (this.m_lowerBoundType == BoundType.INCLUSIVE);
        assert (this.m_upperBoundType == BoundType.INCLUSIVE);
        if (this.m_intervalType == IntervalType.WITHOUT_TIMEZONE) {
            int numberOfValues = 1;
            if (DateTime.isLastDayInstant(this.m_lowerBound)) {
                ++numberOfValues;
            }
            return Math.max(0, argument - numberOfValues);
        }
        int numberOfValues = 1681;
        if (DateTime.secondsAreZero(this.m_lowerBound)) {
            if (this.m_lowerBound >= 0L) {
                int minutesInDay = DateTime.getMinutesInDay(this.m_lowerBound);
                assert (minutesInDay < 1440);
                if (0 <= minutesInDay && minutesInDay <= 840) {
                    ++numberOfValues;
                }
                if (600 <= minutesInDay) {
                    ++numberOfValues;
                }
            } else {
                int minutesInDay = DateTime.getMinutesInDay(this.m_lowerBound);
                assert (-1440 < minutesInDay);
                if (-840 <= minutesInDay && minutesInDay <= 0) {
                    ++numberOfValues;
                }
                if (minutesInDay <= -600) {
                    ++numberOfValues;
                }
            }
        }
        return Math.max(0, argument - numberOfValues);
    }

    public boolean containsDateTime(DateTime dateTime) {
        if (dateTime.hasTimeZoneOffset() ? this.m_intervalType == IntervalType.WITHOUT_TIMEZONE : this.m_intervalType == IntervalType.WITH_TIMEZONE) {
            return false;
        }
        long timeOnTimeline = dateTime.getTimeOnTimeline();
        if (this.m_lowerBound > timeOnTimeline || this.m_lowerBound == timeOnTimeline && this.m_lowerBoundType == BoundType.EXCLUSIVE) {
            return false;
        }
        return this.m_upperBound >= timeOnTimeline && (this.m_upperBound != timeOnTimeline || this.m_upperBoundType != BoundType.EXCLUSIVE);
    }

    public void enumerateDateTimes(Collection<Object> dateTimes) {
        if (this.m_lowerBound == this.m_upperBound) {
            assert (this.m_lowerBoundType == BoundType.INCLUSIVE);
            assert (this.m_upperBoundType == BoundType.INCLUSIVE);
            if (this.m_intervalType == IntervalType.WITHOUT_TIMEZONE) {
                dateTimes.add(new DateTime(this.m_lowerBound, false, Integer.MAX_VALUE));
                if (DateTime.isLastDayInstant(this.m_lowerBound)) {
                    dateTimes.add(new DateTime(this.m_lowerBound, true, Integer.MAX_VALUE));
                }
            } else {
                for (int timeZoneOffset = -840; timeZoneOffset <= 840; ++timeZoneOffset) {
                    dateTimes.add(new DateTime(this.m_lowerBound, false, timeZoneOffset));
                }
                if (DateTime.secondsAreZero(this.m_lowerBound)) {
                    int minutesInDay;
                    if (this.m_lowerBound >= 0L) {
                        minutesInDay = DateTime.getMinutesInDay(this.m_lowerBound);
                        assert (minutesInDay < 1440);
                        if (0 <= minutesInDay && minutesInDay <= 840) {
                            dateTimes.add(new DateTime(this.m_lowerBound, true, - minutesInDay));
                        }
                        if (600 <= minutesInDay) {
                            dateTimes.add(new DateTime(this.m_lowerBound, true, 1440 - minutesInDay));
                        }
                    } else {
                        minutesInDay = DateTime.getMinutesInDay(this.m_lowerBound);
                        assert (-1440 < minutesInDay);
                        if (-840 <= minutesInDay && minutesInDay <= 0) {
                            dateTimes.add(new DateTime(this.m_lowerBound, true, -1440 - minutesInDay));
                        }
                        if (minutesInDay <= -600) {
                            dateTimes.add(new DateTime(this.m_lowerBound, true, - minutesInDay));
                        }
                    }
                }
            }
        } else {
            throw new IllegalStateException("The data range is infinite.");
        }
    }

    protected static boolean isIntervalEmpty(long lowerBound, BoundType lowerBoundType, long upperBound, BoundType upperBoundType) {
        return lowerBound > upperBound || lowerBound == upperBound && (lowerBoundType == BoundType.EXCLUSIVE || upperBoundType == BoundType.EXCLUSIVE);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.m_intervalType.toString());
        if (this.m_lowerBoundType == BoundType.INCLUSIVE) {
            buffer.append('[');
        } else {
            buffer.append('<');
        }
        buffer.append(this.m_lowerBound);
        buffer.append(" .. ");
        buffer.append(this.m_upperBound);
        if (this.m_upperBoundType == BoundType.INCLUSIVE) {
            buffer.append(']');
        } else {
            buffer.append('>');
        }
        return buffer.toString();
    }
}

