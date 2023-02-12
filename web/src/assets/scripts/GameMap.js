import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";

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

        this.inner_walls_count = 20;
        this.walls = [];
    }

    check_connectivity(g, sx, sy, tx, ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = true;

        let dx = [-1, 0, 1, 0], dy = [0, -1, 0, 1];

        for (let i = 0; i < 4; i++) {
            let nx = sx + dx[i], ny = sy + dy[i];
            if (!g[nx][ny] && this.check_connectivity(g, nx, ny, tx, ty)) {
                return true;
            }
        }
        return false;
    }


    create_walls() {
        const g = [];

        for (let i = 0; i < this.rows; i++) {
            g[i] = [];
            for (let j = 0; j < this.cols; j++) {
                g[i][j] = false;
            }
        }

        // 给四周加上墙
        for (let i = 0; i < this.rows; i++) {
            g[i][0] = g[i][this.cols - 1] = true;
        }

        for (let i = 0; i < this.cols; i++) {
            g[0][i] = g[this.rows - 1][i] = true;
        }

        // 创建随机墙
        for (let i = 0; i < this.inner_walls_count / 2; i++) {
            for (let j = 0; j < 1000; j++) {
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                if (g[r][c] || g[c][r]) continue;
                // 左下角和右上角作为起点不能有墙
                if ((r == this.rows - 2 && c == 1) || (r == 1 && c == this.cols - 2))
                    continue;
                g[r][c] = g[c][r] = true;
                break;
            }
        }

        let copy_g = JSON.parse(JSON.stringify(g));
        if (!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)) {
            return false;
        }

        for (let i = 0; i < this.rows; i++) {
            for (let j = 0; j < this.cols; j++) {
                if (g[i][j]) {
                    this.walls.push(new Wall(i, j, this));
                }
            }
        }

        return true;
    }

    start() {
        for (let i = 0; i < 1000; i++) {
            if (this.create_walls()) {
                break;
            }
        }
    }

    


    update_size() {
        // console.log("clientwidth: " + this.parent.clientWidth)
        // console.log("clientheight: " + this.parent.clientHeight)

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
        const color_even = '#A2D149', color_odd = '#AAD751';
        for (let i = 0; i < this.rows; i++) {
            for (let j = 0; j < this.cols; j++) {
                if ((i + j) % 2 == 0) {
                    this.ctx.fillStyle = color_even;
                } else {
                    this.ctx.fillStyle = color_odd;
                }

                this.ctx.fillRect(j * this.L, i * this.L, this.L, this.L);
            }
        }
    }
}