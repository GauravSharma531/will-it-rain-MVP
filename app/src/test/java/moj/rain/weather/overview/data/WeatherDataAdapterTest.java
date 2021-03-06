package moj.rain.weather.overview.data;


import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import io.reactivex.schedulers.Schedulers;
import moj.rain.app.network.model.weather.Hour;
import moj.rain.weather.overview.model.WeatherHour;

import static com.google.common.truth.Truth.assertThat;
import static moj.rain.TestConstants.APPARENT_TEMPERATURE_1;
import static moj.rain.TestConstants.CLOUD_COVER_1;
import static moj.rain.TestConstants.DEW_POINT_1;
import static moj.rain.TestConstants.HUMIDITY_1;
import static moj.rain.TestConstants.ICON_1;
import static moj.rain.TestConstants.O_ZONE_1;
import static moj.rain.TestConstants.PRECIP_INTENSITY_1;
import static moj.rain.TestConstants.PRECIP_PROBABILITY_1;
import static moj.rain.TestConstants.PRESSURE_1;
import static moj.rain.TestConstants.SUMMARY_1;
import static moj.rain.TestConstants.TEMPERATURE_1;
import static moj.rain.TestConstants.TIME_1;
import static moj.rain.TestConstants.WIND_BEARING_1;
import static moj.rain.TestConstants.WIND_SPEED_1;

public class WeatherDataAdapterTest {

    private WeatherDataAdapter weatherDataAdapter;

    private WeatherHour weatherHour;
    private Hour hour;
    private double temperature;
    private double apparentTemperature;
    private double doubleValue;

    private boolean actualBoolean;
    private WeatherHour actualWeatherHour;
    private int actualInt;

    @Before
    public void setUp() throws Exception {
        weatherDataAdapter = new WeatherDataAdapter(
                Schedulers.trampoline(),
                Schedulers.trampoline());
    }

    @Test
    public void isValid_true() throws Exception {
        givenValidWeatherHour();
        whenDestinationDataIsCheckedIfValid();
        thenDestinationDataShouldBeCheckedIfValid(true);
    }

    @Test
    public void isValid_false() throws Exception {
        givenNullWeatherHour();
        whenDestinationDataIsCheckedIfValid();
        thenDestinationDataShouldBeCheckedIfValid(false);
    }

    @Test
    public void transform() throws Exception {
        givenValidHour();
        whenSourceIsTransformed();
        thenSourceShouldBeTransformedToDestination();
    }

    @Test
    public void getRoundedToNearestFive_1() throws Exception {
        givenADouble(5.4);
        whenDoubleIsAdapted();
        thenDoubleShouldReturnCorrectlyAsAnInt(540);
    }

    @Test
    public void getRoundedToNearestFive_2() throws Exception {
        givenADouble(5.434);
        whenDoubleIsAdapted();
        thenDoubleShouldReturnCorrectlyAsAnInt(545);
    }

    @Test
    public void getRoundedToNearestFive_3() throws Exception {
        givenADouble(0.032);
        whenDoubleIsAdapted();
        thenDoubleShouldReturnCorrectlyAsAnInt(5);
    }

    @Test
    public void getTemperature_1() throws Exception {
        givenTemperatureAndApparentTemperatures(1.2, 2.3);
        whenAnAverageTemperatureIsCalculated();
        thenDoubleShouldReturnCorrectlyAsAnInt(2);
    }

    @Test
    public void getTemperature_2() throws Exception {
        givenTemperatureAndApparentTemperatures(10.2, 10.8);
        whenAnAverageTemperatureIsCalculated();
        thenDoubleShouldReturnCorrectlyAsAnInt(11);
    }

    @Test
    public void getTemperature_3() throws Exception {
        givenTemperatureAndApparentTemperatures(-15.2, 15.2);
        whenAnAverageTemperatureIsCalculated();
        thenDoubleShouldReturnCorrectlyAsAnInt(0);
    }

    private void givenValidWeatherHour() {
        weatherHour = WeatherHour.builder()
                .setHour(new DateTime(TIME_1 * 1000))
                .setIcon(ICON_1)
                .setPrecipIntensity(weatherDataAdapter.adaptDouble(PRECIP_INTENSITY_1))
                .setPrecipProbability(weatherDataAdapter.adaptDouble(PRECIP_PROBABILITY_1))
                .setTemperature(weatherDataAdapter.getTemperature(TEMPERATURE_1, APPARENT_TEMPERATURE_1))
                .build();
    }

    private void givenNullWeatherHour() {
        weatherHour = null;
    }

    private void givenValidHour() {
        hour = Hour.builder()
                .setTime(TIME_1)
                .setSummary(SUMMARY_1)
                .setIcon(ICON_1)
                .setPrecipIntensity(PRECIP_INTENSITY_1)
                .setPrecipProbability(PRECIP_PROBABILITY_1)
                .setTemperature(TEMPERATURE_1)
                .setApparentTemperature(APPARENT_TEMPERATURE_1)
                .setDewPoint(DEW_POINT_1)
                .setHumidity(HUMIDITY_1)
                .setWindSpeed(WIND_SPEED_1)
                .setWindBearing(WIND_BEARING_1)
                .setCloudCover(CLOUD_COVER_1)
                .setPressure(PRESSURE_1)
                .setOzone(O_ZONE_1)
                .build();
    }

    private void givenADouble(double expectedDouble) {
        this.doubleValue = expectedDouble;
    }

    private void givenTemperatureAndApparentTemperatures(double temperature, double apparentTemperature) {
        this.temperature = temperature;
        this.apparentTemperature = apparentTemperature;
    }

    private void whenDestinationDataIsCheckedIfValid() {
        actualBoolean = weatherDataAdapter.isValid(weatherHour);
    }

    private void whenSourceIsTransformed() {
        actualWeatherHour = weatherDataAdapter.transformSource(hour);
    }

    private void whenDoubleIsAdapted() {
        actualInt = weatherDataAdapter.adaptDouble(doubleValue);
    }

    private void whenAnAverageTemperatureIsCalculated() {
        actualInt = weatherDataAdapter.getTemperature(temperature, apparentTemperature);
    }

    private void thenDestinationDataShouldBeCheckedIfValid(boolean expected) {
        assertThat(actualBoolean).isEqualTo(expected);
    }

    private void thenSourceShouldBeTransformedToDestination() {
        givenValidWeatherHour();
        assertThat(actualWeatherHour).isEqualTo(weatherHour);
    }

    private void thenDoubleShouldReturnCorrectlyAsAnInt(int expectedInt) {
        assertThat(actualInt).isEqualTo(expectedInt);
    }
}