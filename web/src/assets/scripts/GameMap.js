import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
    constructor(ctx, parent, store) {
        super();
        // console.log("clientheight: " + parent.clientHeight)
        // console.log("clientwidth: " + parent.clientWidth)

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0; // 单位长度

        this.rows = 13
        this.cols = 14;
        
        this.store = store;
        this.walls = [];

        this.snakes = [
            new Snake({id: 0, color: "#4876EC", r: this.rows - 2, c: 1}, this),
            new Snake({id: 1, color: "#F94848", r: 1, c: this.cols - 2}, this),
        ];
    }

    create_walls() {
        let g = this.store.state.pk.gamemap;

        for (let i = 0; i < this.rows; i++) {
            for (let j = 0; j < this.cols; j++) {
                if (g[i][j]) {
                    this.walls.push(new Wall(i, j, this));
                }
            }
        }
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
        this.create_walls();

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