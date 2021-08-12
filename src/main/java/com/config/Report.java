package com.config;

import net.sf.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import net.sf.dynamicreports.report.builder.column.PercentageColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.awt.*;
import java.math.BigDecimal;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class Report {
    public Report() {

    }

    public void build(){
        CurrencyType currencyType = new CurrencyType();

        StyleBuilder boldStyle = stl.style().bold();
        StyleBuilder centerStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        StyleBuilder columnTitleStyle = stl.style(centerStyle)
                .setBorder(stl.pen1Point())
                .setBackgroundColor(Color.LIGHT_GRAY);

        StyleBuilder titleStyle = stl.style(centerStyle)
                .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
                .setFontSize(15);

        TextColumnBuilder<Integer> rowNumberColumn = col.reportRowNumberColumn("No.")
                .setFixedColumns(2)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType()).setStyle(boldStyle);
        TextColumnBuilder<Integer> quantityColumn = col.column("Quantity", "quantity", type.integerType());
        TextColumnBuilder<BigDecimal> unitPriceColumn = col.column("Unit price", "unitprice", currencyType);
        TextColumnBuilder<BigDecimal> priceColumn =
                unitPriceColumn.multiply(quantityColumn).setTitle("Price").setDataType(currencyType);
        PercentageColumnBuilder pricePercColumn = col.percentageColumn("Price %", priceColumn);

        //Add Charts
        Bar3DChartBuilder itemChart = cht.bar3DChart()
                .setTitle("Sales by item")
                .setCategory(itemColumn)
                .addSerie(cht.serie(unitPriceColumn), cht.serie(priceColumn));

        Bar3DChartBuilder itemChart2 = cht.bar3DChart()
                .setTitle("Sales by item")
                .setCategory(itemColumn)
                .setUseSeriesAsCategory(true)
                .addSerie(cht.serie(unitPriceColumn), cht.serie(priceColumn));

        ColumnGroupBuilder itemGroup = grp.group(itemColumn);
        itemGroup.setPrintSubtotalsWhenExpression(exp.printWhenGroupHasMoreThanOneRow(itemGroup));

        ConditionalStyleBuilder styleBuilder = stl.conditionalStyle(cnd.greater(priceColumn, 150))
                .setBackgroundColor(Color.GREEN);

        ConditionalStyleBuilder builder = stl.conditionalStyle(cnd.smaller(priceColumn, 30))
                .setBackgroundColor(Color.RED);

        try {
            report()
                    .setColumnTitleStyle(columnTitleStyle)
                    .setSubtotalStyle(boldStyle)
                    .highlightDetailEvenRows()
                    .columns(
                            rowNumberColumn,
                            itemColumn,
                            quantityColumn,
                            unitPriceColumn,
                            priceColumn,
                            pricePercColumn
                    )
                    .columnGrid(
                            rowNumberColumn,
                            quantityColumn,
                            unitPriceColumn,
                            grid.verticalColumnGridList(priceColumn, pricePercColumn)
                    )
                    .groupBy(itemGroup)
                    .subtotalsAtSummary(sbt.sum(unitPriceColumn), sbt.sum(priceColumn))
                    .subtotalsAtFirstGroupFooter(sbt.sum(unitPriceColumn), sbt.sum(priceColumn))
                    .detailRowHighlighters(styleBuilder, builder)
                    .title(
                            cmp.horizontalList()
                            .add(
                                    cmp.text("DynamicReports").setStyle(titleStyle)
                                            .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
                                    cmp.text("Getting started").setStyle(titleStyle)
                                            .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
                            ).newRow()
                            .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10))
                    )
                    .pageFooter(cmp.pageXofY().setStyle(centerStyle))
                    .summary(cmp.horizontalList(itemChart, itemChart2))
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("item", "quantity", "unitprice");
        dataSource.add("Notebook", 1, new BigDecimal(500));
        dataSource.add("DVD", 5, new BigDecimal(30));
        dataSource.add("DVD", 1, new BigDecimal(28));
        dataSource.add("DVD", 5, new BigDecimal(32));
        dataSource.add("Book", 3, new BigDecimal(11));
        dataSource.add("Book", 1, new BigDecimal(15));
        dataSource.add("Book", 5, new BigDecimal(10));
        dataSource.add("Book", 8, new BigDecimal(9));


        return dataSource;
    }

    private class CurrencyType extends BigDecimalType{
        private static final long serialVersionUID = 1L;

        @Override
        public String getPattern() {
            return "$ #,###.00";
        }
    }
}
