# Raytracer

Laboration 2, Systemutveckling Java, Lernia.

Programmet skickar ut strålar från en kamera in i en 3D-scen, räknar ut vad varje
stråle träffar och sparar resultatet som en bildfil.

## Köra

    mvn clean compile
    java -cp target/classes se.lernia.raytracer.Main

Kräver JDK 25 och Maven. Bilden hamnar i output/ som render.ppm och render.png.

Antal strålar per pixel går att skicka med som argument. Main 1 ger en snabb
körning utan antialiasing, standard är 16. Testerna körs med mvn test.

## Klasserna

Main bygger scenen, kör renderingen och sparar filerna. Det är enda stället i
programmet som vet vilka objekt som finns.

math

Vector3D är en punkt eller en riktning i 3D. Har add, subtract, scale, dot, cross,
length, normalize och reflect. Ray är en stråle, alltså en startpunkt och en
riktning. Riktningen normaliseras i konstruktorn, så t i pointAt är samma sak som
avståndet.

geometry

Shape är ett interface med hit(ray, tMin, tMax) som lämnar tillbaka en
Optional<Hit>. AbstractShape är basklassen som håller namn och material och ser
till att normalen vänds mot strålen. Sphere, Triangle och Plane ärver den och
skriver var sin hit. Hit är informationen man får vid en träff: avstånd, punkt,
normal och vilket objekt som träffades.

material

Material är ett interface med shade, som ger färgen i en punkt på en yta.
AbstractMaterial är basklassen och håller grundfärgen. SolidColor lämnar bara
tillbaka sin färg rakt av. Lambertian räknar ut ljuset. SurfacePoint är det lilla
ett material behöver veta om träffpunkten.

light

PointLight är en lampa med position, färg och styrka. Lighting är ett litet
interface med ljuskällorna, bakgrundsljuset och skuggtestet. Scene implementerar
det, så materialen behöver inte känna till hela scenklassen.

scene

Scene håller objekten i en List<Shape> och lamporna i en List<PointLight>.
closestHit loopar igenom alla objekt och behåller den närmaste träffen, isInShadow
skickar skuggstrålar. Camera bestämmer var man står, vad man tittar på och hur
brett man ser. Renderer går igenom varje pixel, skickar strålarna och fyller en
Image.

image

Color är RGB som tre flyttal mellan 0 och 1. Image är pixlarna i minnet.
ImageWriter är ett interface för att spara en bild. PpmWriter skriver PPM i
textformatet P3, PngWriter skriver PNG med ImageIO som ingår i JDK:n.

## Lägga till en ny Shape

Skriv en klass som ärver AbstractShape. Konstruktorn tar namn och material och
skickar vidare dem med super.

Sen skriver du hit. Räkna ut avståndet t där strålen träffar formen, kolla att t
ligger mellan tMin och tMax, och lämna tillbaka Optional.of(buildHit(ray, t,
normal)). Missar strålen returnerar du Optional.empty(). Normalen behöver du inte
vända åt rätt håll själv, det sköter buildHit.

Sist lägger du objektet i scenen i Main.buildScene() med .add(...). Inget annat
behöver ändras, eftersom Scene och Renderer bara känner till Shape och aldrig de
enskilda formerna.

## VG-delarna

Material och belysning. Material är ett interface, AbstractMaterial en abstrakt
basklass som håller albedo, och SolidColor och Lambertian ärver den. SolidColor
struntar i ljuset och ger alltid sin färg. Lambertian följer Lamberts cosinuslag:
hur mycket ljus en matt yta får beror bara på vinkeln mellan ytans normal och
riktningen mot lampan, och den vinkeln får man ur skalärprodukten av två
normaliserade vektorer. Rakt mot lampan blir ljusast, och när vinkeln passerar 90
grader vetter ytan bort och får inget ljus. I bilden syns skillnaden direkt: den
gula sfären har SolidColor och blir en platt cirkel, de andra är rundade av ljuset.

Skuggor och ljuskällor. PointLight är en egen klass med position, färg och styrka,
och ljuset avtar med kvadraten på avståndet. För varje lampa frågar Lambertian
scenen om punkten ligger i skugga, och Scene.isInShadow skickar då en sekundär
stråle från träffpunkten mot lampan. Träffar den något på vägen hoppas lampan
över. Två saker behövdes: skuggstrålen måste starta en liten bit från ytan, annars
träffar den ytan själv och bilden blir prickig, och den måste ha ett tMax på
avståndet till lampan, annars räknas objekt bakom lampan som skuggande.

Kamera och antialiasing. Camera tar position, blickpunkt, uppåtvektor, synfält och
bildförhållande som konstruktorparametrar och bygger ett eget koordinatsystem av
dem, så en ny vy kräver ingen kodändring. Antialiasing sitter i Renderer: med fler
än en stråle per pixel slumpas strålens läge inom pixeln och medelvärdet blir
pixelns färg. Utan det blir en lutande kant en hård trappa mellan två färger, med
det får trappstegen mellantoner. Slumpen är seedad, så samma inställningar ger
alltid exakt samma bild.
