import { AcGameObject } from "./AcGameObject";

export class GameMap extends AcGameObject {
    constructor(ctx, parent) {
        super();
        // console.log("clientheight: " + parent.clientHeight)
        // console.log("clientwidth: " + parent.clientWidth)

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0; // 单位长度

        this.cols = 13;
        this.rows = 13;
    }

    start() {

    }

    update_size() {
        console.log("clientwidth: " + this.parent.clientWidth)
        console.log("clientheight: " + this.parent.clientHeight)

        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        
        this.ctx.canvas.width = this.cols * this.L;
        this.ctx.canvas.height = this.rows * this.L;

        // console.log("width: " + this.ctx.canvas.width)
        // console.log("height: " + this.ctx.canvas.height)
    }

    update() {
        this.update_size();
        this.render();
    }    

    render() {
        this.ctx.fillStyle = 'green';
        this.ctx.fillRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
    }
}