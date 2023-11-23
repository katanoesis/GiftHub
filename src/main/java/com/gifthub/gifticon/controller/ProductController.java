package com.gifthub.gifticon.controller;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.dto.ProductDto;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.gifticon.service.ProductService;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final GifticonService gifticonService;

    private final String path = "C:/OcrPractice/";

    @GetMapping("/add/excel")
    public void AddProductFromExcel(){
        List<ProductDto> productList = new ArrayList<>();
        String ExcelName = "기프티쇼 비즈_상품리스트_비즈_API.xlsx";
        try {
            FileInputStream file = new FileInputStream(path + ExcelName);
            Workbook workbook = null;


            if(ExcelName.endsWith(".xlsx")){
                workbook = new XSSFWorkbook(file);
            } else if(ExcelName.endsWith(".xls")){
                workbook = new HSSFWorkbook(file);
            } else {
                throw new RuntimeException("file type not matched");
            }


            if(workbook == null){
                throw new RuntimeException("no data in file");
            }

            int rowindx = 0;
            int columnindex = 0;

            Sheet worksheet = workbook.getSheetAt(0);

            int rows = worksheet.getPhysicalNumberOfRows();
            for (rowindx = 13; rowindx < rows; rowindx++){ // 13행부터 한 줄씩 읽는다
                Row row = worksheet.getRow(rowindx);
                if(row != null){ // 행이 비어있지 않다면
                    ProductDto productDto = new ProductDto();
                    productDto.setId((long) row.getCell(0).getNumericCellValue());
                    productDto.setBrandName(row.getCell(1).getStringCellValue());
                    productDto.setName(row.getCell(2).getStringCellValue());
                    productDto.setPrice((long) row.getCell(4).getNumericCellValue());

                    productList.add(productDto);

                }

            }
            productService.saveAll(productList);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/test")
    public void testProductDto(){
        List<ProductDto> productDtoList = productService.getAllProduct();
        for(ProductDto productDto : productDtoList){
            System.out.println(productDto.getName());
            System.out.println(productDto.getBrandName());
            System.out.println(productDto.getPrice());
            System.out.println(productDto.getId());
        }
    }

    // TODO : 구매페이지의 해당 product 클릭시 해당 product_id를 갖는 GifticonList를 가져오기
    @GetMapping("/get/gifticon")
    public void getGifticonByProduct(@Param("productId") Long productId){
        List<GifticonDto> gifticonDtoList = gifticonService.getGifticonByProduct(productId);
        //
    }

    // TODO : 금액별, 남은 유효기간별(임박순) 정렬하기
}
