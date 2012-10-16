mogrify -
find . -iname "*.png" | xargs -l -i convert -resize 32x32 {} {}
