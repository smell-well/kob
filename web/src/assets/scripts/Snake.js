import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell"

export class Snake extends AcGameObject {
    constructor(info, gamemap) {
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;

        this.cells = [new Cell(info.r, info.c)];
        this.next_cell = null;

        this.speed = 5;   // 每秒移动格子

        this.direction = -1; // -1没有指令，0，1，2，3 上右下左
        this.status = "idle" // idle 静止， move 移动， die 死亡
    
        this.dc = [0, 1, 0, -1];
        this.dr = [-1, 0, 1, 0];
    
        this.step = 0;
        this.eps = 1e-2; // 允许的误差

        this.eye_direction = 0;
        if (this.id === 1) this.eye_direction = 2;
        
        this.eye_dx =[
            [-1, 1],
            [1, 1],
            [-1, 1],
            [-1, -1]
        ];
        this.eye_dy =[
            [-1, -1],
            [-1, 1],
            [1, 1],
            [-1, 1]
        ]
    }

    start() {

    }
    
    set_direction(d) {
        this.direction = d;
    }

    check_tail_increasing() {
        if (this.step <= 10) {
            return true;
        }
        if (this.step % 3 === 1) {
            return true;
        }

        return false;
    }


    next_step() {  //将蛇的状态变为下一步
        const d = this.direction;
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.eye_direction = d;
        this.direction = -1; // 清空操作
        this.status = "move"
        this.step++;

        const k = this.cells.length;
        for (let i = k; i > 0; i--) {
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1])); 
        }

        if (!this.gamemap.check_valid(this.next_cell)) {
            this.status = "die";
        }
    }

    update_move() {
        
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < this.eps) {
            this.status = "idle"; // 走完了
            this.cells[0] = this.next_cell; // 添加新的蛇头
            this.next_cell = null; 

            if (!this.check_tail_increasing()) {
                this.cells.pop();
            }
        } else {
            const move_distance = this.speed * this.timedelta / 1000; //每两帧之间的走过的距离
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;

            if (!this.check_tail_increasing()) { // 蛇尾移动
                const k = this.cells.length;
                const tail_dx = this.cells[k - 2].x - this.cells[k - 1].x;
                const tail_dy = this.cells[k - 2].y - this.cells[k - 1].y;
                const tail_distance = Math.sqrt(tail_dx * tail_dx + tail_dy * tail_dy);

                this.cells[k - 1].x += move_distance * tail_dx / tail_distance;
                this.cells[k - 1].y += move_distance * tail_dy / tail_distance;
            }
        }
    }

    update() {  // 每帧执行一次
        if (this.status == "move") {
            this.update_move();
        }
        
        this.render();
    }

    draw_die_eye(x, y) {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;
        
        ctx.beginPath();
        ctx.moveTo((x - 0.07) * L, (y - 0.07) * L);
        ctx.lineTo((x + 0.07) * L, (y + 0.07) * L);

        ctx.moveTo((x + 0.07) * L, (y - 0.07) * L);
        ctx.lineTo((x - 0.07) * L, (y + 0.07) * L);
        ctx.stroke();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        
        

        if (this.status === "die") {
            ctx.fillStyle = "white";
        }

        for (const cell of this.cells) {
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L * 0.4, 0, 2 * Math.PI);
            ctx.fill();
        }

        for (let i = 1; i < this.cells.length; i++) {
            const a = this.cells[i - 1], b = this.cells[i];
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps) {
                continue;
            }

            if (Math.abs(a.x - b.x) < this.eps) { // 竖直情况
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }
        
        ctx.fillStyle = 'black'
        for (let i = 0; i < 2; i++) {
            const eye_x = (this.cells[0].x + 0.15 * this.eye_dx[this.eye_direction][i]);
            const eye_y = (this.cells[0].y + 0.15 * this.eye_dy[this.eye_direction][i]);

            if (this.status === "die") {
                this.draw_die_eye(eye_x, eye_y)
            } else {
                ctx.beginPath();
                ctx.arc(eye_x * L, eye_y * L, L * 0.05, 0, 2 * Math.PI);
                ctx.fill();
            }
        }

    }

}