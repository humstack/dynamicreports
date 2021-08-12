package com.config;

import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.chart.CategoryChartSerieBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
import net.sf.dynamicreports.report.builder.style.ReportStyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.math.BigDecimal;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class SubDatasource {

    public SubDatasource() {
        build();
    }

    private void build(){
        FontBuilder fontBuilder = stl.fontArialBold().setFontSize(15);

        FieldBuilder<String> itemField = field("item", type.stringType());
        FieldBuilder<Integer> quantityField = field("quantity", type.integerType());
        FieldBuilder<BigDecimal> unitPriceField = field("unitprice", type.bigDecimalType());

        CategoryChartSerieBuilder quantitySerie = cht.serie(quantityField).setLabel("Quantity");
        CategoryChartSerieBuilder unitPriceSerie = cht.serie(unitPriceField).setLabel("Unit Price");

        BarChartBuilder chart1 = cht.barChart()
                .setDataSource(createDataSource1())
                .setTitle("SubDatasource 1")
                .setTitleFont(fontBuilder)
                .setCategory(itemField)
                .series(quantitySerie, unitPriceSerie);

        BarChartBuilder chart2 = cht.barChart()
                .setDataSource(createDataSource2())
                .setTitle("SubDatasource 2")
                .setTitleFont(fontBuilder)
                .setCategory(itemField)
                .series(quantitySerie, unitPriceSerie);

        CrosstabRowGroupBuilder<String> rowGroup = ctab
                .rowGroup("state", String.class)
                .setTotalHeader("Total for state");

        CrosstabColumnGroupBuilder<String> columnGroup = ctab
                .columnGroup("item", String.class);

        ReportStyleBuilder boldStyle = stl.style()
                .bold()
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

        CrosstabBuilder crosstab = ctab.crosstab()
                .setDataSource(createDataSource3())
                .headerCell(cmp.text("State / item").setStyle(boldStyle))
                .rowGroups(rowGroup)
                .columnGroups(columnGroup)
                .measures(
                        ctab.measure("Quantity", "quantity", Integer.class, Calculation.SUM),
                        ctab.measure("Unit Price", "unitprice", BigDecimal.class, Calculation.SUM)
                );

        try {
            report()
                    .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
                    .title(
                            cmp.horizontalList(chart1, chart2),
                            cmp.text("SubDatasource 3").setStyle(boldStyle),
                            crosstab
                    ).pageFooter(cmp.pageXofY())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource3() {
        DRDataSource dataSource = new DRDataSource("state", "item", "quantity", "unitprice");
        dataSource.add("New York", "Notebook", 1, new BigDecimal(500));
        dataSource.add("New York", "DVD", 5, new BigDecimal(30));
        dataSource.add("New York", "DVD", 2, new BigDecimal(45));
        dataSource.add("New York", "DVD", 4, new BigDecimal(36));
        dataSource.add("New York", "DVD", 5, new BigDecimal(41));
        dataSource.add("New York", "Book", 2, new BigDecimal(11));
        dataSource.add("New York", "Book", 8, new BigDecimal(9));
        dataSource.add("New York", "Book", 6, new BigDecimal(14));

        dataSource.add("Washington", "Notebook", 1, new BigDecimal(610));
        dataSource.add("Washington", "DVD", 4, new BigDecimal(40));
        dataSource.add("Washington", "DVD", 6, new BigDecimal(35));
        dataSource.add("Washington", "DVD", 3, new BigDecimal(46));
        dataSource.add("Washington", "DVD", 2, new BigDecimal(42));
        dataSource.add("Washington", "Book", 3, new BigDecimal(12));
        dataSource.add("Washington", "Book", 9, new BigDecimal(8));
        dataSource.add("Washington", "Book", 4, new BigDecimal(14));
        dataSource.add("Washington", "Book", 5, new BigDecimal(10));

        dataSource.add("Florida", "Notebook", 1, new BigDecimal(460));
        dataSource.add("Florida", "DVD", 3, new BigDecimal(49));
        dataSource.add("Florida", "DVD", 4, new BigDecimal(32));
        dataSource.add("Florida", "DVD", 2, new BigDecimal(47));
        dataSource.add("Florida", "Book", 4, new BigDecimal(11));
        dataSource.add("Florida", "Book", 8, new BigDecimal(6));
        dataSource.add("Florida", "Book", 6, new BigDecimal(16));
        dataSource.add("Florida", "Book", 3, new BigDecimal(18));
        return dataSource;
    }

    private JRDataSource createDataSource2() {
        DRDataSource dataSource = new DRDataSource("item", "quantity", "unitprice");
        dataSource.add("Book", 100, new BigDecimal(120));
        dataSource.add("Notebook", 190, new BigDecimal(350));
        dataSource.add("PDA", 800, new BigDecimal(290));
        return dataSource;
    }

    private JRDataSource createDataSource1() {
        DRDataSource dataSource = new DRDataSource("item", "quantity", "unitprice");
        dataSource.add("Book", 170, new BigDecimal(100));
        dataSource.add("Notebook", 90, new BigDecimal(450));
        dataSource.add("PDA", 120, new BigDecimal(250));
        return dataSource;
    }

}
