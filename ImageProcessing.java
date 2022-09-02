import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;

public class ImageProcessing {
	public static void main(String[] args) {
    // The provided images are apple.jpg, flower.jpg, and kitten.jpg
		int[][] imageData = imgToTwoD("./apple.jpg");
    // Or load your own image using a URL!
		//int[][] imageData = imgToTwoD("https://content.codecademy.com/projects/project_thumbnails/phaser/bug-dodger.png");
		//viewImageData(imageData);

		int[][] trimmed = trimBorders(imageData, 60);
        twoDToImage(trimmed,"./trimmed_apple.jpg");

		int[][] nagative = negativeColor(imageData);
		twoDToImage(nagative,"./negative_apple.jpg");

		int[][] streched = stretchHorizontally(imageData);
		twoDToImage(streched,"./streched_apple.jpg");

		int[][] shrinked = shrinkVertically(imageData);
		twoDToImage(shrinked,"./shrinked_apple.jpg");

		int[][] inverted = invertImage(imageData);
		twoDToImage(inverted,"./inverted_apple.jpg");

		int[][] colorFilter = colorFilter(imageData,-75,30,30);
		twoDToImage(colorFilter,"./colored_apple.jpg");
        
		int[][] blankImg = new int [400][400];
		int[][] randomImg = paintRandomImage(blankImg);
		twoDToImage(randomImg,"./random_image.jpg");

		int[][] blankImg1 = new int [400][400];
		int[] rgba = {255,255,0,255};
		int color = getColorIntValFromRGBA(rgba);
		int[][] rectangle = paintRectangle(blankImg1, 50, 100, 50, 50, color);
		twoDToImage(rectangle,"./rectangle_image.jpg");

		int[][] blankImg2 = new int [400][400];
		int[][] randomRectangle = generateRectangles(blankImg2,4);
		twoDToImage(randomRectangle,"./randomRectangle_image.jpg");
		// int[][] allFilters = stretchHorizontally(shrinkVertically(colorFilter(negativeColor(trimBorders(invertImage(imageData), 50)), 200, 20, 40)));
		// Painting with pixels
	}

	// Image Processing Methods
	public static int[][] trimBorders(int[][] imageTwoD, int pixelCount) {
		// Trim off of the borders of the image
		if (imageTwoD.length > pixelCount * 2 && imageTwoD[0].length > pixelCount * 2) {
			int[][] trimmedImg = new int[imageTwoD.length - pixelCount * 2][imageTwoD[0].length - pixelCount * 2];
			for (int i = 0; i < trimmedImg.length; i++) {
				for (int j = 0; j < trimmedImg[i].length; j++) {
					trimmedImg[i][j] = imageTwoD[i + pixelCount][j + pixelCount];
				}
			}
			return trimmedImg;
		} else {
			System.out.println("Cannot trim that many pixels from the given image.");
			return imageTwoD;
		}
	}

	public static int[][] negativeColor(int[][] imageTwoD) {
		// Negative Version of the Image
		int[][] negativeImg = new int[imageTwoD.length][imageTwoD[0].length];
		for(int i = 0;i < imageTwoD.length;i++){
			for(int j = 0;j < imageTwoD[0].length;j++){
				int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
				rgba[0] = 255 - rgba[0];
				rgba[1] = 255 - rgba[1];
				rgba[2] = 255 - rgba[2];
				negativeImg[i][j] = getColorIntValFromRGBA(rgba);
			}      
    }
		return negativeImg;
	}

	public static int[][] stretchHorizontally(int[][] imageTwoD) {
		// Stretch the Image Horizontally
		int[][] stretchedImg = new int[imageTwoD.length][imageTwoD[0].length * 2];
		for(int i = 0;i < imageTwoD.length;i++){
			for(int j = 0;j < imageTwoD[0].length;j++){
              stretchedImg[i][2 * j] = imageTwoD[i][j];
			  stretchedImg[i][2 * j + 1] = imageTwoD[i][j];
			}
		}  
		return stretchedImg;
	}

	public static int[][] shrinkVertically(int[][] imageTwoD) {
		// Shrink the Image Vertically
		   // note: how to deal with odd number
		   // 0 - 0; 1 - ; 2 - 1; 3 - ; 4 - 2; 
		int[][] shrinkedImg = new int[imageTwoD.length/2][imageTwoD[0].length];
		for(int i = 0; i < imageTwoD[0].length;i++){
			for(int j = 0;j < imageTwoD.length;j+=2){
				shrinkedImg[j/2][i] = imageTwoD[j][i];
			}
		}
		return shrinkedImg;
	}

	public static int[][] invertImage(int[][] imageTwoD) {
		// Invert the Image
		int[][] invertedImg = new int[imageTwoD.length][imageTwoD.length];
        for(int i = 0;i < imageTwoD.length; i++){
			int row = imageTwoD.length - i - 1;
			for(int j = 0; j < imageTwoD[0].length;j++){
				int col = imageTwoD[0].length - j - 1;
				invertedImg[i][j] = imageTwoD[row][col];
			}
		}
		return invertedImg;
	}

	public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue, int blueChangeValue) {
		// Applying a Color Filter
		// example filter(-75, 30, -30)
		int[][] coloredImg = new int[imageTwoD.length][imageTwoD[0].length];
		for(int i = 0; i < imageTwoD.length;i++){
			for(int j = 0;j < imageTwoD[0].length;j++){
				int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);		        
			    rgba[0] += redChangeValue;
                rgba[1] += greenChangeValue;
                rgba[2] += blueChangeValue;
	
                for(int y = 0; y < 3;y++){
					if(rgba[y] > 255){
					  rgba[y] = 255;
					}
					else if(rgba[y] < 0){
					  rgba[y] = 0;
					}
				}
				coloredImg[i][j] = getColorIntValFromRGBA(rgba);
			}
		}
		return coloredImg;
	}

	// Painting Methods
	public static int[][] paintRandomImage(int[][] canvas) {
		// Painting an Image of Random Colors
		Random rand = new Random();
		for (int i = 0;i < canvas.length;i++){
			for (int j = 0;j < canvas[0].length;j++){
				int randRed = rand.nextInt(256);
				int randGreen = rand.nextInt(256);
				int randBlue = rand.nextInt(256);
				int [] rgba = {randRed,randGreen,randBlue,255}; // Alpha value controls transparency
			    canvas[i][j] = getColorIntValFromRGBA(rgba);
			}
		}	
		return canvas;
	}

	public static int[][] paintRectangle(int[][] canvas, int width, int height, int rowPosition, int colPosition, int color) {
		// Drawing a Rectangle on an Image
        for(int i = rowPosition; i < rowPosition + height;i++){
			for (int j = colPosition;j < colPosition + width;j++){
				canvas[i][j] = color;
			}
		}
		return canvas;
	}

	public static int[][] generateRectangles(int[][] canvas, int numRectangles) {
		// Create Abstract Geometric Art Utilizing the paintRectangle Method
		  //1. start postion of the rectancles ?
		  //2. if rectangle size overbig than the canvas from the start position?
		  //3. overlapping bewteen the rectangles,, color overlapping?
		Random rand = new Random();
		for(int i = 0;i < numRectangles;i++){
			int width = rand.nextInt(canvas[0].length);
			int height = rand.nextInt(canvas.length);      
			int randomRowPos = rand.nextInt(canvas.length - height );
			int randomColPos = rand.nextInt(canvas[0].length - width);
			int red = rand.nextInt(256);
			int green = rand.nextInt(256);
			int blue = rand.nextInt(256);
			int[] rgba = {red,green,blue,255};
			int color = getColorIntValFromRGBA(rgba);
			for(int x = randomRowPos; x < randomRowPos+height;x++){
				for(int y = randomColPos; y < randomColPos+width;y++){
                   canvas[x][y] = color;
				}
			}
			paintRectangle(canvas, width, height, randomRowPos, randomColPos, color);
		}
		return canvas;
	}

	// Utility Methods
	public static int[][] imgToTwoD(String inputFileOrLink) {
		try {
			BufferedImage image = null;
			if (inputFileOrLink.substring(0, 4).toLowerCase().equals("http")) {
				URL imageUrl = new URL(inputFileOrLink);
				image = ImageIO.read(imageUrl);
				if (image == null) {
					System.out.println("Failed to get image from provided URL.");
				}
			} else {
				image = ImageIO.read(new File(inputFileOrLink));
			}
			int imgRows = image.getHeight();
			int imgCols = image.getWidth();
			int[][] pixelData = new int[imgRows][imgCols];
			for (int i = 0; i < imgRows; i++) {
				for (int j = 0; j < imgCols; j++) {
					pixelData[i][j] = image.getRGB(j, i);
				}
			}
			return pixelData;
		} catch (Exception e) {
			System.out.println("Failed to load image: " + e.getLocalizedMessage());
			return null;
		}
	}
	public static void twoDToImage(int[][] imgData, String fileName) {
		try {
			int imgRows = imgData.length;
			int imgCols = imgData[0].length;
			BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < imgRows; i++) {
				for (int j = 0; j < imgCols; j++) {
					result.setRGB(j, i, imgData[i][j]);
				}
			}
			File output = new File(fileName);
			ImageIO.write(result, "jpg", output);
		} catch (Exception e) {
			System.out.println("Failed to save image: " + e.getLocalizedMessage());
		}
	}
	public static int[] getRGBAFromPixel(int pixelColorValue) {
		Color pixelColor = new Color(pixelColorValue);
		return new int[] { pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha() };
	}
	public static int getColorIntValFromRGBA(int[] colorData) {
		if (colorData.length == 4) {
			Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
			return color.getRGB();
		} else {
			System.out.println("Incorrect number of elements in RGBA array.");
			return -1;
		}
	}
	public static void viewImageData(int[][] imageTwoD) {
		if (imageTwoD.length > 3 && imageTwoD[0].length > 3) {
			int[][] rawPixels = new int[3][3];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					rawPixels[i][j] = imageTwoD[i][j];
				}
			}
			System.out.println("Raw pixel data from the top left corner.");
			System.out.print(Arrays.deepToString(rawPixels).replace("],", "],\n") + "\n");
			int[][][] rgbPixels = new int[3][3][4];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					rgbPixels[i][j] = getRGBAFromPixel(imageTwoD[i][j]);
				}
			}
			System.out.println();
			System.out.println("Extracted RGBA pixel data from top the left corner.");
			for (int[][] row : rgbPixels) {
				System.out.print(Arrays.deepToString(row) + System.lineSeparator());
			}
		} else {
			System.out.println("The image is not large enough to extract 9 pixels from the top left corner");
		}
	}
}