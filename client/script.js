let socket
let color = '#000000'
let strokeWidth = 4

const WIDTH = 128 * 8
const HEIGHT = 128 * 5

function setup() {

	const color_picker = select('#pickcolor')
	const color_btn = select('#color-btn')
	const color_holder = select('#color-holder')

	const stroke_width_picker = select('#stroke-width-picker')
	const stroke_btn = select('#stroke-btn')
	const stroke_holder = select('#stroke-holder')
	
	const cv = createCanvas(WIDTH, HEIGHT)
	cv.position(600, 100)
	cv.background(255)
	stroke(color)
	color_holder.style('background-color', color)

	websocket = new WebSocket("ws://localhost:3000")

	color_btn.mousePressed(() => {
		color = color_picker.value()
		color_holder.style('background-color', color)
	})

	stroke_btn.mousePressed(() => {
		const width = parseInt(stroke_width_picker.value())
		if (width > 0) strokeWidth = width
		stroke_holder.html(strokeWidth)
	})
}

function mouseDragged() {
	stroke(color)
	strokeWeight(strokeWidth)
	line(mouseX, mouseY, pmouseX, pmouseY)

	sendmouse(mouseX, mouseY, pmouseX, pmouseY)
}


function sendmouse(x, y, pX, pY) {
	if (x < 0 || x > WIDTH
		|| y < 0 || y > HEIGHT) return;
	const data = {
		x: Math.round(x),
		y: Math.round(y),
		px: Math.round(pX),
		py: Math.round(pY),
		color: color,
		strokeWidth: strokeWidth,
	}

	websocket.send(JSON.stringify(data))
}