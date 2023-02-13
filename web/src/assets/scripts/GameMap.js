import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
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
        this.rows = 14;

        this.inner_walls_count = 20;
        this.walls = [];

        this.snakes = [
            new Snake({id: 0, color: "#4876EC", r: this.rows - 2, c: 1}, this),
            new Snake({id: 1, color: "#F94848", r: 1, c: this.cols - 2}, this),
        ];
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
                if (g[r][c] || g[this.rows - 1 - r][this.cols - 1 - c]) continue;
                // 左下角和右上角作为起点不能有墙
                if ((r == this.rows - 2 && c == 1) || (r == 1 && c == this.cols - 2))
                    continue;
                // 中心对称
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = true;
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

    add_listening_events() {
        this.ctx.canvas.focus();

        const [snake0, snake1] = this.snakes
        this.ctx.canvas.addEventListener('keydown', e => {
            if (e.key === 'w') snake0.set_direction(0);
            else if (e.key === 'd') snake0.set_direction(1);
            else if (e.key === 's') snake0.set_direction(2);
            else if (e.key === 'a') snake0.set_direction(3);
            else if (e.key === 'ArrowUp') snake1.set_direction(0);
            else if (e.key === 'ArrowRight') snake1.set_direction(1);
            else if (e.key === 'ArrowDown') snake1.set_direction(2);
            else if (e.key === 'ArrowLeft') snake1.set_direction(3);
        });
    }

    start() {
        for (let i = 0; i < 1000; i++) {
            if (this.create_walls()) {
                break;
            }
        }

        this.add_listening_events();
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

    check_ready() {  //判断两条蛇都准备好了下一回合
        for (const snake of this.snakes) {
            if (snake.status !== "idle") return false;
            if (snake.direction === -1) return false;
        }

        return true;
    }

    // 判断下一个位置可行性
    check_valid(cell) {
        for (let wall of this.walls) {
            if (wall.r === cell.r && wall.c === cell.c) {
                return false;
            }
        }

        for (let snake of this.snakes) {
            let k = snake.cells.length;
            if (!snake.check_tail_increasing()) { // 蛇尾会移动
                k--;
            }
            for (let i = 0; i < k; i++) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) {
                    return false;
                }
            }
        }

        return true;
    }


    next_step() { // 让两条蛇进入下一回合
        for (const snake of this.snakes) {
            snake.next_step();
        }
    }


    update() {
        this.update_size();
        if (this.check_ready()) {
            this.next_step();
        }
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